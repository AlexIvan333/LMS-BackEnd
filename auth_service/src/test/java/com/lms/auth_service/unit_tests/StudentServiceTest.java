package com.lms.auth_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lms.auth_service.configurations.authetication.PasswordGenerator;
import com.lms.auth_service.dtos.filters.UserFilterParams;
import com.lms.auth_service.dtos.requests.CreateUserRequest;
import com.lms.auth_service.dtos.responses.ServiceResult;
import com.lms.auth_service.dtos.responses.StudentResponse;
import com.lms.auth_service.entities.enums.Role;
import com.lms.auth_service.entities.relational.StudentEntity;
import com.lms.auth_service.repositories.relational.StudentRepository;
import com.lms.auth_service.repositories.relational.UserRepository;
import com.lms.auth_service.services.EmailService;
import com.lms.auth_service.services.Google2FAService;
import com.lms.auth_service.services.StudentService;
import com.lms.auth_service.validation.UserValidator;
import com.lms.auth_service.validation.intrefaces.IUserValidator;

import java.util.ArrayList;
import java.util.List;

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

@ContextConfiguration(classes = {StudentService.class, PasswordEncoder.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @MockitoBean
    private EmailService emailService;

    @MockitoBean
    private Google2FAService google2FAService;

    @MockitoBean
    private IUserValidator iUserValidator;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private PasswordGenerator passwordGenerator;

    @MockitoBean
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;


    @Test
    @DisplayName("Test registerStudent(CreateUserRequest)")
    
    void testRegisterStudent() {
        // Arrange
        when(iUserValidator.HasValidEmail(Mockito.any())).thenReturn(true);

        // Act
        ServiceResult<StudentResponse> actualRegisterStudentResult = studentService
                .registerStudent(new CreateUserRequest());

        // Assert
        verify(iUserValidator).HasValidEmail(isNull());
        assertEquals("There already is an account with this email address.", actualRegisterStudentResult.getMessageError());
        assertNull(actualRegisterStudentResult.getData());
        assertEquals(HttpStatus.BAD_REQUEST, actualRegisterStudentResult.getHttpStatus());
        assertFalse(actualRegisterStudentResult.isSuccess());
    }


    @Test
    @DisplayName("Test registerStudent(CreateUserRequest); given StudentEntity() Active is 'true'; then return Data Id is 'null'")
    
    void testRegisterStudent_givenStudentEntityActiveIsTrue_thenReturnDataIdIsNull() {
        // Arrange
        var studentEntity = getStudentEntity();
        when(studentRepository.save(Mockito.any())).thenReturn(studentEntity);
        when(iUserValidator.HasValidEmail(Mockito.any())).thenReturn(false);
        when(passwordEncoder.encode(Mockito.any())).thenReturn("secret");
        when(passwordGenerator.generateRandomPassword()).thenReturn("password");
        when(google2FAService.generateSecretKey()).thenReturn("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
        doNothing().when(emailService)
                .sendEmailWithQRCode(Mockito.any(), Mockito.any(), Mockito.any(),
                        Mockito.any());

        CreateUserRequest request = new CreateUserRequest();
        request.setStreetNumber(10);

        // Act
        ServiceResult<StudentResponse> actualRegisterStudentResult = studentService.registerStudent(request);

        // Assert
        verify(passwordGenerator).generateRandomPassword();
        verify(emailService).sendEmailWithQRCode(isNull(), eq("Welcome to LMS - Your Credentials"), eq("password"),
                eq("otpauth://totp/LMS:null?secret=EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY&issuer=LMS"));
        verify(google2FAService).generateSecretKey();
        verify(iUserValidator).HasValidEmail(isNull());
        verify(studentRepository).save(isA(StudentEntity.class));
        verify(passwordEncoder).encode(isA(CharSequence.class));
        StudentResponse data = actualRegisterStudentResult.getData();
        assertNull(data.getId());
        assertNull(actualRegisterStudentResult.getMessageError());
        assertNull(data.getCity());
        assertNull(data.getCountry());
        assertNull(data.getEmail());
        assertNull(data.getFirstName());
        assertNull(data.getLastName());
        assertNull(data.getMiddleName());
        assertNull(data.getStreetName());
        assertNull(data.getZipCode());
        assertEquals(10, data.getStreetNumber());
        assertEquals(Role.STUDENT, data.getRole());
        assertEquals(HttpStatus.CREATED, actualRegisterStudentResult.getHttpStatus());
        assertTrue(actualRegisterStudentResult.isSuccess());
        assertTrue(data.getActive());
    }

    private static StudentEntity getStudentEntity() {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setActive(true);
        studentEntity.setCity("Oxford");
        studentEntity.setCodeForgotPassword("password");
        studentEntity.setCountry("GB");
        studentEntity.setEmail("jane.doe@example.org");
        studentEntity.setFirstName("Jane");
        studentEntity.setId(1L);
        studentEntity.setLastName("Doe");
        studentEntity.setMiddleName("Middle Name");
        studentEntity.setPassword("password");
        studentEntity.setRole(Role.STUDENT);
        studentEntity.setStreetName("Street Name");
        studentEntity.setStreetNumber(10);
        studentEntity.setTwoFactorCode(3);
        studentEntity.setTwoFactorSecretKey("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
        studentEntity.setZipCode("21654");
        return studentEntity;
    }


    @Test
    @DisplayName("Test getStudents(UserFilterParams); given StudentEntity() Active is 'false'; then return size is two")
    
    void testGetStudents_givenStudentEntityActiveIsFalse_thenReturnSizeIsTwo() {
        // Arrange
        var studentEntity = getStudentEntity();

        var studentEntity2 = getStudentEntity2();

        ArrayList<StudentEntity> content = new ArrayList<>();
        content.add(studentEntity2);
        content.add(studentEntity);
        when(studentRepository.findStudentEntitiesByFilters(Mockito.<Long>any(), Mockito.<Boolean>any(),
                Mockito.any())).thenReturn(new PageImpl<>(content));

        UserFilterParams filterParams = new UserFilterParams();
        filterParams.setActive(true);
        filterParams.setPage(1);
        filterParams.setSize(3);
        filterParams.setStudentId(1L);

        // Act
        List<StudentResponse> actualStudents = studentService.getStudents(filterParams);

        // Assert
        verify(studentRepository).findStudentEntitiesByFilters(eq(1L), eq(true), isA(Pageable.class));
        assertEquals(2, actualStudents.size());
        StudentResponse getResult = actualStudents.get(1);
        assertEquals("21654", getResult.getZipCode());
        assertEquals("Doe", getResult.getLastName());
        assertEquals("GB", getResult.getCountry());
        StudentResponse getResult2 = actualStudents.getFirst();
        assertEquals("GBR", getResult2.getCountry());
        assertEquals("Jane", getResult.getFirstName());
        assertEquals("John", getResult2.getFirstName());
        assertEquals("London", getResult2.getCity());
        assertEquals("Middle Name", getResult.getMiddleName());
        assertEquals("OX1 1PT", getResult2.getZipCode());
        assertEquals("Oxford", getResult.getCity());
        assertEquals("Smith", getResult2.getLastName());
        assertEquals("Street Name", getResult.getStreetName());
        assertEquals("com.lms.auth_service.entities.relational.StudentEntity", getResult2.getMiddleName());
        assertEquals("com.lms.auth_service.entities.relational.StudentEntity", getResult2.getStreetName());
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

    private static StudentEntity getStudentEntity2() {
        StudentEntity studentEntity2 = new StudentEntity();
        studentEntity2.setActive(false);
        studentEntity2.setCity("London");
        studentEntity2.setCodeForgotPassword("Code Forgot Password");
        studentEntity2.setCountry("GBR");
        studentEntity2.setEmail("john.smith@example.org");
        studentEntity2.setFirstName("John");
        studentEntity2.setId(2L);
        studentEntity2.setLastName("Smith");
        studentEntity2.setMiddleName("com.lms.auth_service.entities.relational.StudentEntity");
        studentEntity2.setPassword("Password");
        studentEntity2.setRole(Role.INSTRUCTOR);
        studentEntity2.setStreetName("com.lms.auth_service.entities.relational.StudentEntity");
        studentEntity2.setStreetNumber(1);
        studentEntity2.setTwoFactorCode(1);
        studentEntity2.setTwoFactorSecretKey("Two Factor Secret Key");
        studentEntity2.setZipCode("OX1 1PT");
        return studentEntity2;
    }


    @Test
    @DisplayName("Test getStudents(UserFilterParams); given StudentEntity() Active is 'true'; then return size is one")
    
    void testGetStudents_givenStudentEntityActiveIsTrue_thenReturnSizeIsOne() {
        // Arrange
        var studentEntity = getStudentEntity();

        ArrayList<StudentEntity> content = new ArrayList<>();
        content.add(studentEntity);
        when(studentRepository.findStudentEntitiesByFilters(Mockito.<Long>any(), Mockito.<Boolean>any(),
                Mockito.any())).thenReturn(new PageImpl<>(content));

        UserFilterParams filterParams = new UserFilterParams();
        filterParams.setActive(true);
        filterParams.setPage(1);
        filterParams.setSize(3);
        filterParams.setStudentId(1L);

        // Act
        List<StudentResponse> actualStudents = studentService.getStudents(filterParams);

        // Assert
        verify(studentRepository).findStudentEntitiesByFilters(eq(1L), eq(true), isA(Pageable.class));
        assertEquals(1, actualStudents.size());
        StudentResponse getResult = actualStudents.getFirst();
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
    @DisplayName("Test getStudents(UserFilterParams); given StudentEntity getRole() return 'STUDENT'; then calls getActive()")
    
    void testGetStudents_givenStudentEntityGetRoleReturnStudent_thenCallsGetActive() {
        // Arrange
        StudentEntity studentEntity = mock(StudentEntity.class);
        when(studentEntity.getRole()).thenReturn(Role.STUDENT);
        when(studentEntity.getStreetNumber()).thenReturn(10);
        when(studentEntity.getActive()).thenReturn(true);
        when(studentEntity.getId()).thenReturn(1L);
        when(studentEntity.getCity()).thenReturn("Oxford");
        when(studentEntity.getCountry()).thenReturn("GB");
        when(studentEntity.getEmail()).thenReturn("jane.doe@example.org");
        when(studentEntity.getFirstName()).thenReturn("Jane");
        when(studentEntity.getLastName()).thenReturn("Doe");
        when(studentEntity.getMiddleName()).thenReturn("Middle Name");
        when(studentEntity.getStreetName()).thenReturn("Street Name");
        when(studentEntity.getZipCode()).thenReturn("21654");
        doNothing().when(studentEntity).setActive(Mockito.<Boolean>any());
        doNothing().when(studentEntity).setCity(Mockito.any());
        doNothing().when(studentEntity).setCodeForgotPassword(Mockito.any());
        doNothing().when(studentEntity).setCountry(Mockito.any());
        doNothing().when(studentEntity).setEmail(Mockito.any());
        doNothing().when(studentEntity).setFirstName(Mockito.any());
        doNothing().when(studentEntity).setId(Mockito.<Long>any());
        doNothing().when(studentEntity).setLastName(Mockito.any());
        doNothing().when(studentEntity).setMiddleName(Mockito.any());
        doNothing().when(studentEntity).setPassword(Mockito.any());
        doNothing().when(studentEntity).setRole(Mockito.any());
        doNothing().when(studentEntity).setStreetName(Mockito.any());
        doNothing().when(studentEntity).setStreetNumber(anyInt());
        doNothing().when(studentEntity).setTwoFactorCode(Mockito.<Integer>any());
        doNothing().when(studentEntity).setTwoFactorSecretKey(Mockito.any());
        doNothing().when(studentEntity).setZipCode(Mockito.any());
        studentEntity.setActive(true);
        studentEntity.setCity("Oxford");
        studentEntity.setCodeForgotPassword("password");
        studentEntity.setCountry("GB");
        studentEntity.setEmail("jane.doe@example.org");
        studentEntity.setFirstName("Jane");
        studentEntity.setId(1L);
        studentEntity.setLastName("Doe");
        studentEntity.setMiddleName("Middle Name");
        studentEntity.setPassword("password");
        studentEntity.setRole(Role.STUDENT);
        studentEntity.setStreetName("Street Name");
        studentEntity.setStreetNumber(10);
        studentEntity.setTwoFactorCode(3);
        studentEntity.setTwoFactorSecretKey("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
        studentEntity.setZipCode("21654");

        ArrayList<StudentEntity> content = new ArrayList<>();
        content.add(studentEntity);
        StudentRepository studentRepository = mock(StudentRepository.class);
        when(studentRepository.findStudentEntitiesByFilters(Mockito.<Long>any(), Mockito.<Boolean>any(),
                Mockito.any())).thenReturn(new PageImpl<>(content));
        UserValidator userValidation = new UserValidator(mock(UserRepository.class));
        var actualStudents = getStudentResponses(studentRepository, userValidation);

        // Assert
        verify(studentEntity).getActive();
        verify(studentEntity).getCity();
        verify(studentEntity).getCountry();
        verify(studentEntity).getEmail();
        verify(studentEntity).getFirstName();
        verify(studentEntity).getId();
        verify(studentEntity).getLastName();
        verify(studentEntity).getMiddleName();
        verify(studentEntity).getRole();
        verify(studentEntity).getStreetName();
        verify(studentEntity).getStreetNumber();
        verify(studentEntity).getZipCode();
        verify(studentEntity).setActive(eq(true));
        verify(studentEntity).setCity(eq("Oxford"));
        verify(studentEntity).setCodeForgotPassword(eq("password"));
        verify(studentEntity).setCountry(eq("GB"));
        verify(studentEntity).setEmail(eq("jane.doe@example.org"));
        verify(studentEntity).setFirstName(eq("Jane"));
        verify(studentEntity).setId(eq(1L));
        verify(studentEntity).setLastName(eq("Doe"));
        verify(studentEntity).setMiddleName(eq("Middle Name"));
        verify(studentEntity).setPassword(eq("password"));
        verify(studentEntity).setRole(eq(Role.STUDENT));
        verify(studentEntity).setStreetName(eq("Street Name"));
        verify(studentEntity).setStreetNumber(eq(10));
        verify(studentEntity).setTwoFactorCode(eq(3));
        verify(studentEntity).setTwoFactorSecretKey(eq("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY"));
        verify(studentEntity).setZipCode(eq("21654"));
        verify(studentRepository).findStudentEntitiesByFilters(eq(1L), eq(true), isA(Pageable.class));
        assertEquals(1, actualStudents.size());
        StudentResponse getResult = actualStudents.getFirst();
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

    private static List<StudentResponse> getStudentResponses(StudentRepository studentRepository, UserValidator userValidation) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        Google2FAService google2FAService = new Google2FAService();
        StudentService studentService = new StudentService(studentRepository, userValidation, passwordEncoder,
                passwordGenerator, google2FAService, new EmailService(new JavaMailSenderImpl()));

        UserFilterParams filterParams = new UserFilterParams();
        filterParams.setActive(true);
        filterParams.setPage(1);
        filterParams.setSize(3);
        filterParams.setStudentId(1L);

        // Act
        return studentService.getStudents(filterParams);
    }


    @Test
    @DisplayName("Test getStudents(UserFilterParams); then return Empty")
    
    void testGetStudents_thenReturnEmpty() {
        // Arrange
        when(studentRepository.findStudentEntitiesByFilters(Mockito.<Long>any(), Mockito.<Boolean>any(),
                Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        UserFilterParams filterParams = new UserFilterParams();
        filterParams.setActive(true);
        filterParams.setPage(1);
        filterParams.setSize(3);
        filterParams.setStudentId(1L);

        // Act
        List<StudentResponse> actualStudents = studentService.getStudents(filterParams);

        // Assert
        verify(studentRepository).findStudentEntitiesByFilters(eq(1L), eq(true), isA(Pageable.class));
        assertTrue(actualStudents.isEmpty());
    }
}
