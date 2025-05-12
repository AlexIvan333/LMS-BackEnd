package com.lms.auth_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.diffblue.cover.annotations.MethodsUnderTest;
import com.lms.auth_service.configurations.authetication.PasswordGenerator;
import com.lms.auth_service.dtos.filters.InstructorFilterParams;
import com.lms.auth_service.dtos.requests.CreateUserRequest;
import com.lms.auth_service.dtos.responses.InstructorResponse;
import com.lms.auth_service.dtos.responses.ServiceResult;
import com.lms.auth_service.entities.enums.Role;
import com.lms.auth_service.entities.relational.AdminEntity;
import com.lms.auth_service.entities.relational.InstructorEntity;
import com.lms.auth_service.entities.relational.UserEntity;
import com.lms.auth_service.repositories.relational.InstructorRepository;
import com.lms.auth_service.repositories.relational.UserRepository;
import com.lms.auth_service.services.EmailService;
import com.lms.auth_service.services.Google2FAService;
import com.lms.auth_service.services.InstructorService;
import com.lms.auth_service.validation.UserValidator;
import com.lms.auth_service.validation.intrefaces.IUserValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {InstructorService.class, PasswordEncoder.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
@ExtendWith(MockitoExtension.class)
class InstructorServiceTest {
    @MockitoBean
    private EmailService emailService;

    @MockitoBean
    private Google2FAService google2FAService;

    @MockitoBean
    private IUserValidator iUserValidator;

    @MockitoBean
    private InstructorRepository instructorRepository;

    @Autowired
    private InstructorService instructorService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private PasswordGenerator passwordGenerator;

    @MockitoBean
    private UserRepository userRepository;

    
    @Test
    @DisplayName("Test registerInstructor(CreateUserRequest)")
    
    @MethodsUnderTest({"ServiceResult InstructorService.registerInstructor(CreateUserRequest)"})
    void testRegisterInstructor() {
        // Arrange
        when(iUserValidator.HasValidEmail(Mockito.any())).thenReturn(true);

        // Act
        ServiceResult<InstructorResponse> actualRegisterInstructorResult = instructorService
                .registerInstructor(new CreateUserRequest());

        // Assert
        verify(iUserValidator).HasValidEmail(isNull());
        assertEquals("There already is an account with this email address",
                actualRegisterInstructorResult.getMessageError());
        assertNull(actualRegisterInstructorResult.getData());
        assertEquals(HttpStatus.BAD_REQUEST, actualRegisterInstructorResult.getHttpStatus());
        assertFalse(actualRegisterInstructorResult.isSuccess());
    }

    
    @Test
    @DisplayName("Test registerInstructor(CreateUserRequest); given InstructorEntity() Active is 'true'; then return Data Id is 'null'")
    
    @MethodsUnderTest({"ServiceResult InstructorService.registerInstructor(CreateUserRequest)"})
    void testRegisterInstructor_givenInstructorEntityActiveIsTrue_thenReturnDataIdIsNull() {
        // Arrange
        var instructorEntity = getInstructorEntity();
        when(instructorRepository.save(Mockito.any())).thenReturn(instructorEntity);
        when(passwordEncoder.encode(Mockito.any())).thenReturn("secret");
        when(passwordGenerator.generateRandomPassword()).thenReturn("password");
        when(iUserValidator.HasValidEmail(Mockito.any())).thenReturn(false);
        when(google2FAService.generateSecretKey()).thenReturn("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
        doNothing().when(emailService)
                .sendEmailWithQRCode(Mockito.any(), Mockito.any(), Mockito.any(),
                        Mockito.any());

        CreateUserRequest request = new CreateUserRequest();
        request.setStreetNumber(10);

        // Act
        ServiceResult<InstructorResponse> actualRegisterInstructorResult = instructorService.registerInstructor(request);

        // Assert
        verify(passwordGenerator).generateRandomPassword();
        verify(emailService).sendEmailWithQRCode(isNull(), eq("Welcome to LMS - Your Credentials"), eq("password"),
                eq("otpauth://totp/LMS:null?secret=EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY&issuer=LMS"));
        verify(google2FAService).generateSecretKey();
        verify(iUserValidator).HasValidEmail(isNull());
        verify(instructorRepository).save(isA(InstructorEntity.class));
        verify(passwordEncoder).encode(isA(CharSequence.class));
        InstructorResponse data = actualRegisterInstructorResult.getData();
        assertNull(data.getId());
        assertNull(data.getCity());
        assertNull(data.getCountry());
        assertNull(data.getEmail());
        assertNull(data.getFirstName());
        assertNull(data.getLastName());
        assertNull(data.getMiddleName());
        assertNull(data.getStreetName());
        assertNull(data.getZipCode());
        assertNull(actualRegisterInstructorResult.getMessageError());
        assertEquals(10, data.getStreetNumber());
        assertEquals(Role.INSTRUCTOR, data.getRole());
        assertEquals(HttpStatus.CREATED, actualRegisterInstructorResult.getHttpStatus());
        assertTrue(data.getActive());
        assertTrue(actualRegisterInstructorResult.isSuccess());
    }

    private static InstructorEntity getInstructorEntity() {
        InstructorEntity instructorEntity = new InstructorEntity();
        instructorEntity.setActive(true);
        instructorEntity.setCity("Oxford");
        instructorEntity.setCodeForgotPassword("password");
        instructorEntity.setCountry("GB");
        instructorEntity.setEmail("jane.doe@example.org");
        instructorEntity.setFirstName("Jane");
        instructorEntity.setId(1L);
        instructorEntity.setLastName("Doe");
        instructorEntity.setMiddleName("Middle Name");
        instructorEntity.setPassword("password");
        instructorEntity.setRole(Role.STUDENT);
        instructorEntity.setStreetName("Street Name");
        instructorEntity.setStreetNumber(10);
        instructorEntity.setTwoFactorCode(3);
        instructorEntity.setTwoFactorSecretKey("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
        instructorEntity.setValidatedByAdmin(true);
        instructorEntity.setZipCode("21654");
        return instructorEntity;
    }

   
    @Test
    @DisplayName("Test getInstructors(InstructorFilterParams); given InstructorEntity() Active is 'false'; then return size is two")
    
    @MethodsUnderTest({"List InstructorService.getInstructors(InstructorFilterParams)"})
    void testGetInstructors_givenInstructorEntityActiveIsFalse_thenReturnSizeIsTwo() {
        // Arrange
        var instructorEntity = getInstructorEntity();

        var instructorEntity2 = getInstructorEntity2();

        ArrayList<InstructorEntity> content = new ArrayList<>();
        content.add(instructorEntity2);
        content.add(instructorEntity);
        when(instructorRepository.findInstructorEntitiesByFilters(Mockito.<Long>any(), Mockito.<Boolean>any(),
                Mockito.any(),Mockito.any())).thenReturn(new PageImpl<>(content));

        InstructorFilterParams filterParams = new InstructorFilterParams();
        filterParams.setActive(true);
        filterParams.setInstructorID(1L);
        filterParams.setPage(1);
        filterParams.setSize(3);

        // Act
        List<InstructorResponse> actualInstructors = instructorService.getInstructors(filterParams);

        // Assert
        verify(instructorRepository).findInstructorEntitiesByFilters(eq(1L), eq(true),nullable(String.class), isA(Pageable.class));
        assertEquals(2, actualInstructors.size());
        InstructorResponse getResult = actualInstructors.get(1);
        assertEquals("21654", getResult.getZipCode());
        assertEquals("Doe", getResult.getLastName());
        assertEquals("GB", getResult.getCountry());
        InstructorResponse getResult2 = actualInstructors.getFirst();
        assertEquals("GBR", getResult2.getCountry());
        assertEquals("Jane", getResult.getFirstName());
        assertEquals("John", getResult2.getFirstName());
        assertEquals("London", getResult2.getCity());
        assertEquals("Middle Name", getResult.getMiddleName());
        assertEquals("OX1 1PT", getResult2.getZipCode());
        assertEquals("Oxford", getResult.getCity());
        assertEquals("Smith", getResult2.getLastName());
        assertEquals("Street Name", getResult.getStreetName());
        assertEquals("com.lms.auth_service.entities.relational.InstructorEntity", getResult2.getMiddleName());
        assertEquals("com.lms.auth_service.entities.relational.InstructorEntity", getResult2.getStreetName());
        assertEquals("jane.doe@example.org", getResult.getEmail());
        assertEquals("john.smith@example.org", getResult2.getEmail());
        assertEquals(1, getResult2.getStreetNumber());
        assertEquals(10, getResult.getStreetNumber());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals(2L, getResult2.getId().longValue());
        assertEquals(Role.INSTRUCTOR, getResult2.getRole());
        assertEquals(Role.STUDENT, getResult.getRole());
        assertFalse(getResult2.getActive());
        assertTrue(getResult.getActive());
    }

    private static InstructorEntity getInstructorEntity2() {
        InstructorEntity instructorEntity2 = new InstructorEntity();
        instructorEntity2.setActive(false);
        instructorEntity2.setCity("London");
        instructorEntity2.setCodeForgotPassword("Code Forgot Password");
        instructorEntity2.setCountry("GBR");
        instructorEntity2.setEmail("john.smith@example.org");
        instructorEntity2.setFirstName("John");
        instructorEntity2.setId(2L);
        instructorEntity2.setLastName("Smith");
        instructorEntity2.setMiddleName("com.lms.auth_service.entities.relational.InstructorEntity");
        instructorEntity2.setPassword("Password");
        instructorEntity2.setRole(Role.INSTRUCTOR);
        instructorEntity2.setStreetName("com.lms.auth_service.entities.relational.InstructorEntity");
        instructorEntity2.setStreetNumber(1);
        instructorEntity2.setTwoFactorCode(1);
        instructorEntity2.setTwoFactorSecretKey("Two Factor Secret Key");
        instructorEntity2.setValidatedByAdmin(false);
        instructorEntity2.setZipCode("OX1 1PT");
        return instructorEntity2;
    }

    
    @Test
    @DisplayName("Test getInstructors(InstructorFilterParams); given InstructorEntity() Active is 'true'; then return size is one")
    
    @MethodsUnderTest({"List InstructorService.getInstructors(InstructorFilterParams)"})
    void testGetInstructors_givenInstructorEntityActiveIsTrue_thenReturnSizeIsOne() {
        // Arrange
        var instructorEntity = getInstructorEntity();

        ArrayList<InstructorEntity> content = new ArrayList<>();
        content.add(instructorEntity);
        when(instructorRepository.findInstructorEntitiesByFilters(Mockito.<Long>any(), Mockito.<Boolean>any(),
                Mockito.any(),Mockito.any())).thenReturn(new PageImpl<>(content));

        InstructorFilterParams filterParams = new InstructorFilterParams();
        filterParams.setActive(true);
        filterParams.setInstructorID(1L);
        filterParams.setPage(1);
        filterParams.setSize(3);

        // Act
        List<InstructorResponse> actualInstructors = instructorService.getInstructors(filterParams);

        // Assert
        verify(instructorRepository).findInstructorEntitiesByFilters(eq(1L), eq(true),nullable(String.class), isA(Pageable.class));
        assertEquals(1, actualInstructors.size());
        InstructorResponse getResult = actualInstructors.getFirst();
        assertEquals("21654", getResult.getZipCode());
        assertEquals("Doe", getResult.getLastName());
        assertEquals("GB", getResult.getCountry());
        assertEquals("Jane", getResult.getFirstName());
        assertEquals("Middle Name", getResult.getMiddleName());
        assertEquals("Oxford", getResult.getCity());
        assertEquals("Street Name", getResult.getStreetName());
        assertEquals("jane.doe@example.org", getResult.getEmail());
        assertEquals(10, getResult.getStreetNumber());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals(Role.STUDENT, getResult.getRole());
        assertTrue(getResult.getActive());
    }

   
    @Test
    @DisplayName("Test getInstructors(InstructorFilterParams); then calls setValidatedByAdmin(boolean)")
    
    @MethodsUnderTest({"List InstructorService.getInstructors(InstructorFilterParams)"})
    void testGetInstructors_thenCallsSetValidatedByAdmin() {
        // Arrange
        InstructorEntity instructorEntity = mock(InstructorEntity.class);
        when(instructorEntity.getRole()).thenReturn(Role.STUDENT);
        when(instructorEntity.getStreetNumber()).thenReturn(10);
        when(instructorEntity.getActive()).thenReturn(true);
        when(instructorEntity.getId()).thenReturn(1L);
        when(instructorEntity.getCity()).thenReturn("Oxford");
        when(instructorEntity.getCountry()).thenReturn("GB");
        when(instructorEntity.getEmail()).thenReturn("jane.doe@example.org");
        when(instructorEntity.getFirstName()).thenReturn("Jane");
        when(instructorEntity.getLastName()).thenReturn("Doe");
        when(instructorEntity.getMiddleName()).thenReturn("Middle Name");
        when(instructorEntity.getStreetName()).thenReturn("Street Name");
        when(instructorEntity.getZipCode()).thenReturn("21654");
        doNothing().when(instructorEntity).setValidatedByAdmin(anyBoolean());
        doNothing().when(instructorEntity).setActive(Mockito.<Boolean>any());
        doNothing().when(instructorEntity).setCity(Mockito.any());
        doNothing().when(instructorEntity).setCodeForgotPassword(Mockito.any());
        doNothing().when(instructorEntity).setCountry(Mockito.any());
        doNothing().when(instructorEntity).setEmail(Mockito.any());
        doNothing().when(instructorEntity).setFirstName(Mockito.any());
        doNothing().when(instructorEntity).setId(Mockito.<Long>any());
        doNothing().when(instructorEntity).setLastName(Mockito.any());
        doNothing().when(instructorEntity).setMiddleName(Mockito.any());
        doNothing().when(instructorEntity).setPassword(Mockito.any());
        doNothing().when(instructorEntity).setRole(Mockito.any());
        doNothing().when(instructorEntity).setStreetName(Mockito.any());
        doNothing().when(instructorEntity).setStreetNumber(anyInt());
        doNothing().when(instructorEntity).setTwoFactorCode(Mockito.<Integer>any());
        doNothing().when(instructorEntity).setTwoFactorSecretKey(Mockito.any());
        doNothing().when(instructorEntity).setZipCode(Mockito.any());
        instructorEntity.setActive(true);
        instructorEntity.setCity("Oxford");
        instructorEntity.setCodeForgotPassword("password");
        instructorEntity.setCountry("GB");
        instructorEntity.setEmail("jane.doe@example.org");
        instructorEntity.setFirstName("Jane");
        instructorEntity.setId(1L);
        instructorEntity.setLastName("Doe");
        instructorEntity.setMiddleName("Middle Name");
        instructorEntity.setPassword("password");
        instructorEntity.setRole(Role.STUDENT);
        instructorEntity.setStreetName("Street Name");
        instructorEntity.setStreetNumber(10);
        instructorEntity.setTwoFactorCode(3);
        instructorEntity.setTwoFactorSecretKey("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
        instructorEntity.setValidatedByAdmin(true);
        instructorEntity.setZipCode("21654");

        ArrayList<InstructorEntity> content = new ArrayList<>();
        content.add(instructorEntity);
        when(instructorRepository.findInstructorEntitiesByFilters(Mockito.<Long>any(), Mockito.<Boolean>any(),
                Mockito.any(),Mockito.any())).thenReturn(new PageImpl<>(content));

        InstructorFilterParams filterParams = new InstructorFilterParams();
        filterParams.setActive(true);
        filterParams.setInstructorID(1L);
        filterParams.setPage(1);
        filterParams.setSize(3);

        // Act
        List<InstructorResponse> actualInstructors = instructorService.getInstructors(filterParams);

        // Assert
        verify(instructorEntity).setValidatedByAdmin(eq(true));
        verify(instructorEntity).getActive();
        verify(instructorEntity).getCity();
        verify(instructorEntity).getCountry();
        verify(instructorEntity).getEmail();
        verify(instructorEntity).getFirstName();
        verify(instructorEntity).getId();
        verify(instructorEntity).getLastName();
        verify(instructorEntity).getMiddleName();
        verify(instructorEntity).getRole();
        verify(instructorEntity).getStreetName();
        verify(instructorEntity).getStreetNumber();
        verify(instructorEntity).getZipCode();
        verify(instructorEntity).setActive(eq(true));
        verify(instructorEntity).setCity(eq("Oxford"));
        verify(instructorEntity).setCodeForgotPassword(eq("password"));
        verify(instructorEntity).setCountry(eq("GB"));
        verify(instructorEntity).setEmail(eq("jane.doe@example.org"));
        verify(instructorEntity).setFirstName(eq("Jane"));
        verify(instructorEntity).setId(eq(1L));
        verify(instructorEntity).setLastName(eq("Doe"));
        verify(instructorEntity).setMiddleName(eq("Middle Name"));
        verify(instructorEntity).setPassword(eq("password"));
        verify(instructorEntity).setRole(eq(Role.STUDENT));
        verify(instructorEntity).setStreetName(eq("Street Name"));
        verify(instructorEntity).setStreetNumber(eq(10));
        verify(instructorEntity).setTwoFactorCode(eq(3));
        verify(instructorEntity).setTwoFactorSecretKey(eq("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY"));
        verify(instructorEntity).setZipCode(eq("21654"));
        verify(instructorRepository).findInstructorEntitiesByFilters(eq(1L), eq(true),nullable(String.class), isA(Pageable.class));
        assertEquals(1, actualInstructors.size());
        InstructorResponse getResult = actualInstructors.getFirst();
        assertEquals("21654", getResult.getZipCode());
        assertEquals("Doe", getResult.getLastName());
        assertEquals("GB", getResult.getCountry());
        assertEquals("Jane", getResult.getFirstName());
        assertEquals("Middle Name", getResult.getMiddleName());
        assertEquals("Oxford", getResult.getCity());
        assertEquals("Street Name", getResult.getStreetName());
        assertEquals("jane.doe@example.org", getResult.getEmail());
        assertEquals(10, getResult.getStreetNumber());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals(Role.STUDENT, getResult.getRole());
        assertTrue(getResult.getActive());
    }

    
    @Test
    @DisplayName("Test getInstructors(InstructorFilterParams); then return Empty")
    
    @MethodsUnderTest({"List InstructorService.getInstructors(InstructorFilterParams)"})
    void testGetInstructors_thenReturnEmpty() {
        // Arrange
        when(instructorRepository.findInstructorEntitiesByFilters(Mockito.<Long>any(), Mockito.<Boolean>any(),
                Mockito.any(),Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        InstructorFilterParams filterParams = new InstructorFilterParams();
        filterParams.setActive(true);
        filterParams.setInstructorID(1L);
        filterParams.setPage(1);
        filterParams.setSize(3);

        // Act
        List<InstructorResponse> actualInstructors = instructorService.getInstructors(filterParams);

        // Assert
        verify(instructorRepository).findInstructorEntitiesByFilters(eq(1L), eq(true),nullable(String.class), isA(Pageable.class));
        assertTrue(actualInstructors.isEmpty());
    }

    
    @Test
    @DisplayName("Test IsValidatedByAdmin(Long)")
    
    @MethodsUnderTest({"ServiceResult InstructorService.IsValidatedByAdmin(Long)"})
    void testIsValidatedByAdmin() {
        // Arrange
        InstructorEntity instructorEntity = mock(InstructorEntity.class);
        doNothing().when(instructorEntity).setValidatedByAdmin(anyBoolean());
        doNothing().when(instructorEntity).setActive(Mockito.<Boolean>any());
        doNothing().when(instructorEntity).setCity(Mockito.any());
        doNothing().when(instructorEntity).setCodeForgotPassword(Mockito.any());
        doNothing().when(instructorEntity).setCountry(Mockito.any());
        doNothing().when(instructorEntity).setEmail(Mockito.any());
        doNothing().when(instructorEntity).setFirstName(Mockito.any());
        doNothing().when(instructorEntity).setId(Mockito.<Long>any());
        doNothing().when(instructorEntity).setLastName(Mockito.any());
        doNothing().when(instructorEntity).setMiddleName(Mockito.any());
        doNothing().when(instructorEntity).setPassword(Mockito.any());
        doNothing().when(instructorEntity).setRole(Mockito.any());
        doNothing().when(instructorEntity).setStreetName(Mockito.any());
        doNothing().when(instructorEntity).setStreetNumber(anyInt());
        doNothing().when(instructorEntity).setTwoFactorCode(Mockito.<Integer>any());
        doNothing().when(instructorEntity).setTwoFactorSecretKey(Mockito.any());
        doNothing().when(instructorEntity).setZipCode(Mockito.any());
        instructorEntity.setActive(true);
        instructorEntity.setCity("Oxford");
        instructorEntity.setCodeForgotPassword("password");
        instructorEntity.setCountry("GB");
        instructorEntity.setEmail("jane.doe@example.org");
        instructorEntity.setFirstName("Jane");
        instructorEntity.setId(1L);
        instructorEntity.setLastName("Doe");
        instructorEntity.setMiddleName("Middle Name");
        instructorEntity.setPassword("password");
        instructorEntity.setRole(Role.STUDENT);
        instructorEntity.setStreetName("Street Name");
        instructorEntity.setStreetNumber(10);
        instructorEntity.setTwoFactorCode(3);
        instructorEntity.setTwoFactorSecretKey("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
        instructorEntity.setValidatedByAdmin(true);
        instructorEntity.setZipCode("21654");
        when(iUserValidator.IsIdValid(Mockito.<Long>any())).thenReturn(false);

        // Act
        ServiceResult<Boolean> actualIsValidatedByAdminResult = instructorService.IsValidatedByAdmin(1L);

        // Assert
        verify(instructorEntity).setValidatedByAdmin(eq(true));
        verify(instructorEntity).setActive(eq(true));
        verify(instructorEntity).setCity(eq("Oxford"));
        verify(instructorEntity).setCodeForgotPassword(eq("password"));
        verify(instructorEntity).setCountry(eq("GB"));
        verify(instructorEntity).setEmail(eq("jane.doe@example.org"));
        verify(instructorEntity).setFirstName(eq("Jane"));
        verify(instructorEntity).setId(eq(1L));
        verify(instructorEntity).setLastName(eq("Doe"));
        verify(instructorEntity).setMiddleName(eq("Middle Name"));
        verify(instructorEntity).setPassword(eq("password"));
        verify(instructorEntity).setRole(eq(Role.STUDENT));
        verify(instructorEntity).setStreetName(eq("Street Name"));
        verify(instructorEntity).setStreetNumber(eq(10));
        verify(instructorEntity).setTwoFactorCode(eq(3));
        verify(instructorEntity).setTwoFactorSecretKey(eq("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY"));
        verify(instructorEntity).setZipCode(eq("21654"));
        verify(iUserValidator).IsIdValid(eq(1L));
        assertEquals("There is no user with this id in the database.", actualIsValidatedByAdminResult.getMessageError());
        assertNull(actualIsValidatedByAdminResult.getData());
        assertEquals(HttpStatus.NOT_FOUND, actualIsValidatedByAdminResult.getHttpStatus());
        assertFalse(actualIsValidatedByAdminResult.isSuccess());
    }

    
    @Test
    @DisplayName("Test IsValidatedByAdmin(Long); given IUserValidator IsInstructor(Long) return 'false'")
    
    @MethodsUnderTest({"ServiceResult InstructorService.IsValidatedByAdmin(Long)"})
    void testIsValidatedByAdmin_givenIUserValidatorIsInstructorReturnFalse() {
        // Arrange
        InstructorEntity instructorEntity = mock(InstructorEntity.class);
        doNothing().when(instructorEntity).setValidatedByAdmin(anyBoolean());
        doNothing().when(instructorEntity).setActive(Mockito.<Boolean>any());
        doNothing().when(instructorEntity).setCity(Mockito.any());
        doNothing().when(instructorEntity).setCodeForgotPassword(Mockito.any());
        doNothing().when(instructorEntity).setCountry(Mockito.any());
        doNothing().when(instructorEntity).setEmail(Mockito.any());
        doNothing().when(instructorEntity).setFirstName(Mockito.any());
        doNothing().when(instructorEntity).setId(Mockito.<Long>any());
        doNothing().when(instructorEntity).setLastName(Mockito.any());
        doNothing().when(instructorEntity).setMiddleName(Mockito.any());
        doNothing().when(instructorEntity).setPassword(Mockito.any());
        doNothing().when(instructorEntity).setRole(Mockito.any());
        doNothing().when(instructorEntity).setStreetName(Mockito.any());
        doNothing().when(instructorEntity).setStreetNumber(anyInt());
        doNothing().when(instructorEntity).setTwoFactorCode(Mockito.<Integer>any());
        doNothing().when(instructorEntity).setTwoFactorSecretKey(Mockito.any());
        doNothing().when(instructorEntity).setZipCode(Mockito.any());
        instructorEntity.setActive(true);
        instructorEntity.setCity("Oxford");
        instructorEntity.setCodeForgotPassword("password");
        instructorEntity.setCountry("GB");
        instructorEntity.setEmail("jane.doe@example.org");
        instructorEntity.setFirstName("Jane");
        instructorEntity.setId(1L);
        instructorEntity.setLastName("Doe");
        instructorEntity.setMiddleName("Middle Name");
        instructorEntity.setPassword("password");
        instructorEntity.setRole(Role.STUDENT);
        instructorEntity.setStreetName("Street Name");
        instructorEntity.setStreetNumber(10);
        instructorEntity.setTwoFactorCode(3);
        instructorEntity.setTwoFactorSecretKey("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
        instructorEntity.setValidatedByAdmin(true);
        instructorEntity.setZipCode("21654");
        when(iUserValidator.IsInstructor(Mockito.<Long>any())).thenReturn(false);
        when(iUserValidator.IsIdValid(Mockito.<Long>any())).thenReturn(true);

        // Act
        ServiceResult<Boolean> actualIsValidatedByAdminResult = instructorService.IsValidatedByAdmin(1L);

        // Assert
        verify(instructorEntity).setValidatedByAdmin(eq(true));
        verify(instructorEntity).setActive(eq(true));
        verify(instructorEntity).setCity(eq("Oxford"));
        verify(instructorEntity).setCodeForgotPassword(eq("password"));
        verify(instructorEntity).setCountry(eq("GB"));
        verify(instructorEntity).setEmail(eq("jane.doe@example.org"));
        verify(instructorEntity).setFirstName(eq("Jane"));
        verify(instructorEntity).setId(eq(1L));
        verify(instructorEntity).setLastName(eq("Doe"));
        verify(instructorEntity).setMiddleName(eq("Middle Name"));
        verify(instructorEntity).setPassword(eq("password"));
        verify(instructorEntity).setRole(eq(Role.STUDENT));
        verify(instructorEntity).setStreetName(eq("Street Name"));
        verify(instructorEntity).setStreetNumber(eq(10));
        verify(instructorEntity).setTwoFactorCode(eq(3));
        verify(instructorEntity).setTwoFactorSecretKey(eq("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY"));
        verify(instructorEntity).setZipCode(eq("21654"));
        verify(iUserValidator).IsIdValid(eq(1L));
        verify(iUserValidator).IsInstructor(eq(1L));
        assertEquals("This use is not an Instructor", actualIsValidatedByAdminResult.getMessageError());
        assertNull(actualIsValidatedByAdminResult.getData());
        assertEquals(HttpStatus.NOT_FOUND, actualIsValidatedByAdminResult.getHttpStatus());
        assertFalse(actualIsValidatedByAdminResult.isSuccess());
    }

   
    @Test
    @DisplayName("Test IsValidatedByAdmin(Long); given InstructorEntity() Active is 'true'")
    
    @MethodsUnderTest({"ServiceResult InstructorService.IsValidatedByAdmin(Long)"})
    void testIsValidatedByAdmin_givenInstructorEntityActiveIsTrue() {
        // Arrange
        var instructorEntity = getInstructorEntity();
        when(instructorRepository.getInstructorEntityById(Mockito.<Long>any())).thenReturn(instructorEntity);
        when(iUserValidator.IsInstructor(Mockito.<Long>any())).thenReturn(true);
        when(iUserValidator.IsIdValid(Mockito.<Long>any())).thenReturn(true);

        // Act
        ServiceResult<Boolean> actualIsValidatedByAdminResult = instructorService.IsValidatedByAdmin(1L);

        // Assert
        verify(instructorRepository).getInstructorEntityById(eq(1L));
        verify(iUserValidator).IsIdValid(eq(1L));
        verify(iUserValidator).IsInstructor(eq(1L));
        assertNull(actualIsValidatedByAdminResult.getMessageError());
        assertEquals(HttpStatus.OK, actualIsValidatedByAdminResult.getHttpStatus());
        assertTrue(actualIsValidatedByAdminResult.getData());
        assertTrue(actualIsValidatedByAdminResult.isSuccess());
    }
    
    @Test
    @DisplayName("Test IsValidatedByAdmin(Long); given UserEntity() Active is 'true'; then calls findById(Long)")
    
    @MethodsUnderTest({"ServiceResult InstructorService.IsValidatedByAdmin(Long)"})
    void testIsValidatedByAdmin_givenUserEntityActiveIsTrue_thenCallsFindById() {
        // Arrange
        var userEntity = getUserEntity();
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        UserValidator userValidation = new UserValidator(userRepository);
        InstructorRepository instructorRepository = mock(InstructorRepository.class);
        UserRepository userRepository2 = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        Google2FAService google2FAService = new Google2FAService();

        // Act
        ServiceResult<Boolean> actualIsValidatedByAdminResult = (new InstructorService(instructorRepository,
                userRepository2, passwordEncoder, passwordGenerator, userValidation, google2FAService,
                new EmailService(new JavaMailSenderImpl()))).IsValidatedByAdmin(1L);

        // Assert
        verify(userRepository).findById(eq(1L));
        verify(userRepository).existsById(eq(1L));
        assertEquals("This use is not an Instructor", actualIsValidatedByAdminResult.getMessageError());
        assertNull(actualIsValidatedByAdminResult.getData());
        assertEquals(HttpStatus.NOT_FOUND, actualIsValidatedByAdminResult.getHttpStatus());
        assertFalse(actualIsValidatedByAdminResult.isSuccess());
    }

    private static UserEntity getUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setActive(true);
        userEntity.setCity("Oxford");
        userEntity.setCodeForgotPassword("password");
        userEntity.setCountry("GB");
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setFirstName("Jane");
        userEntity.setId(1L);
        userEntity.setLastName("Doe");
        userEntity.setMiddleName("Middle Name");
        userEntity.setPassword("password");
        userEntity.setRole(Role.STUDENT);
        userEntity.setStreetName("Street Name");
        userEntity.setStreetNumber(10);
        userEntity.setTwoFactorCode(3);
        userEntity.setTwoFactorSecretKey("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
        userEntity.setZipCode("21654");
        return userEntity;
    }

    
    @Test
    @DisplayName("Test IsValidatedByAdmin(Long); then calls isValidatedByAdmin()")
    
    @MethodsUnderTest({"ServiceResult InstructorService.IsValidatedByAdmin(Long)"})
    void testIsValidatedByAdmin_thenCallsIsValidatedByAdmin() {
        // Arrange
        InstructorEntity instructorEntity = mock(InstructorEntity.class);
        when(instructorEntity.isValidatedByAdmin()).thenReturn(true);
        doNothing().when(instructorEntity).setValidatedByAdmin(anyBoolean());
        doNothing().when(instructorEntity).setActive(Mockito.<Boolean>any());
        doNothing().when(instructorEntity).setCity(Mockito.any());
        doNothing().when(instructorEntity).setCodeForgotPassword(Mockito.any());
        doNothing().when(instructorEntity).setCountry(Mockito.any());
        doNothing().when(instructorEntity).setEmail(Mockito.any());
        doNothing().when(instructorEntity).setFirstName(Mockito.any());
        doNothing().when(instructorEntity).setId(Mockito.<Long>any());
        doNothing().when(instructorEntity).setLastName(Mockito.any());
        doNothing().when(instructorEntity).setMiddleName(Mockito.any());
        doNothing().when(instructorEntity).setPassword(Mockito.any());
        doNothing().when(instructorEntity).setRole(Mockito.any());
        doNothing().when(instructorEntity).setStreetName(Mockito.any());
        doNothing().when(instructorEntity).setStreetNumber(anyInt());
        doNothing().when(instructorEntity).setTwoFactorCode(Mockito.<Integer>any());
        doNothing().when(instructorEntity).setTwoFactorSecretKey(Mockito.any());
        doNothing().when(instructorEntity).setZipCode(Mockito.any());
        instructorEntity.setActive(true);
        instructorEntity.setCity("Oxford");
        instructorEntity.setCodeForgotPassword("password");
        instructorEntity.setCountry("GB");
        instructorEntity.setEmail("jane.doe@example.org");
        instructorEntity.setFirstName("Jane");
        instructorEntity.setId(1L);
        instructorEntity.setLastName("Doe");
        instructorEntity.setMiddleName("Middle Name");
        instructorEntity.setPassword("password");
        instructorEntity.setRole(Role.STUDENT);
        instructorEntity.setStreetName("Street Name");
        instructorEntity.setStreetNumber(10);
        instructorEntity.setTwoFactorCode(3);
        instructorEntity.setTwoFactorSecretKey("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
        instructorEntity.setValidatedByAdmin(true);
        instructorEntity.setZipCode("21654");
        when(instructorRepository.getInstructorEntityById(Mockito.<Long>any())).thenReturn(instructorEntity);
        when(iUserValidator.IsInstructor(Mockito.<Long>any())).thenReturn(true);
        when(iUserValidator.IsIdValid(Mockito.<Long>any())).thenReturn(true);

        // Act
        ServiceResult<Boolean> actualIsValidatedByAdminResult = instructorService.IsValidatedByAdmin(1L);

        // Assert
        verify(instructorEntity).isValidatedByAdmin();
        verify(instructorEntity).setValidatedByAdmin(eq(true));
        verify(instructorEntity).setActive(eq(true));
        verify(instructorEntity).setCity(eq("Oxford"));
        verify(instructorEntity).setCodeForgotPassword(eq("password"));
        verify(instructorEntity).setCountry(eq("GB"));
        verify(instructorEntity).setEmail(eq("jane.doe@example.org"));
        verify(instructorEntity).setFirstName(eq("Jane"));
        verify(instructorEntity).setId(eq(1L));
        verify(instructorEntity).setLastName(eq("Doe"));
        verify(instructorEntity).setMiddleName(eq("Middle Name"));
        verify(instructorEntity).setPassword(eq("password"));
        verify(instructorEntity).setRole(eq(Role.STUDENT));
        verify(instructorEntity).setStreetName(eq("Street Name"));
        verify(instructorEntity).setStreetNumber(eq(10));
        verify(instructorEntity).setTwoFactorCode(eq(3));
        verify(instructorEntity).setTwoFactorSecretKey(eq("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY"));
        verify(instructorEntity).setZipCode(eq("21654"));
        verify(instructorRepository).getInstructorEntityById(eq(1L));
        verify(iUserValidator).IsIdValid(eq(1L));
        verify(iUserValidator).IsInstructor(eq(1L));
        assertNull(actualIsValidatedByAdminResult.getMessageError());
        assertEquals(HttpStatus.OK, actualIsValidatedByAdminResult.getHttpStatus());
        assertTrue(actualIsValidatedByAdminResult.getData());
        assertTrue(actualIsValidatedByAdminResult.isSuccess());
    }

    
    @Test
    @DisplayName("Test getIdOFInstructorByEmail(String); given AdminEntity getId() return one; then calls getId()")
    
    @MethodsUnderTest({"Long InstructorService.getIdOFInstructorByEmail(String)"})
    void testGetIdOFInstructorByEmail_givenAdminEntityGetIdReturnOne_thenCallsGetId() {
        // Arrange
        AdminEntity adminEntity = mock(AdminEntity.class);
        when(adminEntity.getId()).thenReturn(1L);
        doNothing().when(adminEntity).setActive(Mockito.<Boolean>any());
        doNothing().when(adminEntity).setCity(Mockito.any());
        doNothing().when(adminEntity).setCodeForgotPassword(Mockito.any());
        doNothing().when(adminEntity).setCountry(Mockito.any());
        doNothing().when(adminEntity).setEmail(Mockito.any());
        doNothing().when(adminEntity).setFirstName(Mockito.any());
        doNothing().when(adminEntity).setId(Mockito.<Long>any());
        doNothing().when(adminEntity).setLastName(Mockito.any());
        doNothing().when(adminEntity).setMiddleName(Mockito.any());
        doNothing().when(adminEntity).setPassword(Mockito.any());
        doNothing().when(adminEntity).setRole(Mockito.any());
        doNothing().when(adminEntity).setStreetName(Mockito.any());
        doNothing().when(adminEntity).setStreetNumber(anyInt());
        doNothing().when(adminEntity).setTwoFactorCode(Mockito.<Integer>any());
        doNothing().when(adminEntity).setTwoFactorSecretKey(Mockito.any());
        doNothing().when(adminEntity).setZipCode(Mockito.any());
        adminEntity.setActive(true);
        adminEntity.setCity("Oxford");
        adminEntity.setCodeForgotPassword("password");
        adminEntity.setCountry("GB");
        adminEntity.setEmail("jane.doe@example.org");
        adminEntity.setFirstName("Jane");
        adminEntity.setId(1L);
        adminEntity.setLastName("Doe");
        adminEntity.setMiddleName("Middle Name");
        adminEntity.setPassword("password");
        adminEntity.setRole(Role.STUDENT);
        adminEntity.setStreetName("Street Name");
        adminEntity.setStreetNumber(10);
        adminEntity.setTwoFactorCode(3);
        adminEntity.setTwoFactorSecretKey("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
        adminEntity.setZipCode("21654");
        Optional<UserEntity> ofResult = Optional.of(adminEntity);
        when(userRepository.findByEmail(Mockito.any())).thenReturn(ofResult);

        // Act
        Long actualIdOFInstructorByEmail = instructorService.getIdOFInstructorByEmail("jane.doe@example.org");

        // Assert
        verify(adminEntity).getId();
        verify(adminEntity).setActive(eq(true));
        verify(adminEntity).setCity(eq("Oxford"));
        verify(adminEntity).setCodeForgotPassword(eq("password"));
        verify(adminEntity).setCountry(eq("GB"));
        verify(adminEntity).setEmail(eq("jane.doe@example.org"));
        verify(adminEntity).setFirstName(eq("Jane"));
        verify(adminEntity).setId(eq(1L));
        verify(adminEntity).setLastName(eq("Doe"));
        verify(adminEntity).setMiddleName(eq("Middle Name"));
        verify(adminEntity).setPassword(eq("password"));
        verify(adminEntity).setRole(eq(Role.STUDENT));
        verify(adminEntity).setStreetName(eq("Street Name"));
        verify(adminEntity).setStreetNumber(eq(10));
        verify(adminEntity).setTwoFactorCode(eq(3));
        verify(adminEntity).setTwoFactorSecretKey(eq("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY"));
        verify(adminEntity).setZipCode(eq("21654"));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        assertEquals(1L, actualIdOFInstructorByEmail.longValue());
    }

   
    @Test
    @DisplayName("Test getIdOFInstructorByEmail(String); given UserEntity() Active is 'true'")
    
    @MethodsUnderTest({"Long InstructorService.getIdOFInstructorByEmail(String)"})
    void testGetIdOFInstructorByEmail_givenUserEntityActiveIsTrue() {
        // Arrange
        var userEntity = getUserEntity();
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findByEmail(Mockito.any())).thenReturn(ofResult);

        // Act
        Long actualIdOFInstructorByEmail = instructorService.getIdOFInstructorByEmail("jane.doe@example.org");

        // Assert
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        assertEquals(1L, actualIdOFInstructorByEmail.longValue());
    }
}
