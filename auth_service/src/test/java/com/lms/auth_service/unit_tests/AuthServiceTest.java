package com.lms.auth_service.unit_tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.diffblue.cover.annotations.MethodsUnderTest;
import com.lms.auth_service.configurations.authetication.JwtUtil;
import com.lms.auth_service.dtos.requests.LoginRequest;
import com.lms.auth_service.dtos.requests.TwoFactorRequest;
import com.lms.auth_service.dtos.responses.LoginResponse;
import com.lms.auth_service.dtos.responses.ServiceResult;
import com.lms.auth_service.entities.enums.Role;
import com.lms.auth_service.entities.relational.AdminEntity;
import com.lms.auth_service.entities.relational.UserEntity;
import com.lms.auth_service.exceptions.InvalidCredentialsException;
import com.lms.auth_service.exceptions.TwoFactorAuthenticationException;
import com.lms.auth_service.repositories.relational.UserRepository;
import com.lms.auth_service.services.AuthService;
import com.lms.auth_service.services.EmailService;
import com.lms.auth_service.services.Google2FAService;
import com.lms.auth_service.validation.intrefaces.IPasswordValidator;
import com.lms.auth_service.validation.intrefaces.IUserValidator;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuthService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @MockitoBean
    private EmailService emailService;

    @MockitoBean
    private Google2FAService google2FAService;

    @MockitoBean
    private IPasswordValidator iPasswordValidator;

    @MockitoBean
    private IUserValidator iUserValidator;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private UserRepository userRepository;


    @Test
    @DisplayName("Test forgotPassword(String, String, String)")
    
    @MethodsUnderTest({"ServiceResult AuthService.forgotPassword(String, String, String)"})
    void testForgotPassword() {
        // Arrange
        AdminEntity adminEntity = mock(AdminEntity.class);
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
//        Optional.of(adminEntity);
        when(iUserValidator.HasValidEmail(Mockito.any())).thenReturn(false);

        // Act
        ServiceResult<Boolean> actualForgotPasswordResult = authService.forgotPassword("jane.doe@example.org", "password",
                "Verification Code");

        // Assert
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
        verify(iUserValidator).HasValidEmail(eq("jane.doe@example.org"));
        assertEquals("There already is no account with this email address.", actualForgotPasswordResult.getMessageError());
        assertNull(actualForgotPasswordResult.getData());
        assertEquals(HttpStatus.NOT_FOUND, actualForgotPasswordResult.getHttpStatus());
        assertFalse(actualForgotPasswordResult.isSuccess());
    }


    @Test
    @DisplayName("Test forgotPassword(String, String, String)")
    
    @MethodsUnderTest({"ServiceResult AuthService.forgotPassword(String, String, String)"})
    void testForgotPassword2() {
        // Arrange
        AdminEntity adminEntity = mock(AdminEntity.class);
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
        Optional.of(adminEntity);
        when(iUserValidator.HasValidEmail(Mockito.any())).thenReturn(true);
        when(iPasswordValidator.isValid(Mockito.any())).thenReturn(false);

        // Act
        ServiceResult<Boolean> actualForgotPasswordResult = authService.forgotPassword("jane.doe@example.org", "password",
                "Verification Code");

        // Assert
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
        verify(iPasswordValidator).isValid(eq("password"));
        verify(iUserValidator).HasValidEmail(eq("jane.doe@example.org"));
        assertEquals("The password should match the requirements below", actualForgotPasswordResult.getMessageError());
        assertNull(actualForgotPasswordResult.getData());
        assertEquals(HttpStatus.BAD_REQUEST, actualForgotPasswordResult.getHttpStatus());
        assertFalse(actualForgotPasswordResult.isSuccess());
    }


    @Test
    @DisplayName("Test forgotPassword(String, String, String)")
    
    @MethodsUnderTest({"ServiceResult AuthService.forgotPassword(String, String, String)"})
    void testForgotPassword3() {
        // Arrange
        Optional<UserEntity> emptyResult = Optional.empty();
        when(userRepository.findByEmail(Mockito.any())).thenReturn(emptyResult);
        when(iUserValidator.HasValidEmail(Mockito.any())).thenReturn(true);
        when(iPasswordValidator.isValid(Mockito.any())).thenReturn(true);

        // Act
        ServiceResult<Boolean> actualForgotPasswordResult = authService.forgotPassword("jane.doe@example.org", "password",
                "Verification Code");

        // Assert
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        verify(iPasswordValidator).isValid(eq("password"));
        verify(iUserValidator).HasValidEmail(eq("jane.doe@example.org"));
        assertEquals("No account found with the provided email address.", actualForgotPasswordResult.getMessageError());
        assertNull(actualForgotPasswordResult.getData());
        assertEquals(HttpStatus.NOT_FOUND, actualForgotPasswordResult.getHttpStatus());
        assertFalse(actualForgotPasswordResult.isSuccess());
    }


    @Test
    @DisplayName("Test forgotPassword(String, String, String); then return MessageError is 'The verification code is incorrect.'")
    
    @MethodsUnderTest({"ServiceResult AuthService.forgotPassword(String, String, String)"})
    void testForgotPassword_thenReturnMessageErrorIsTheVerificationCodeIsIncorrect() {
        // Arrange
        var userEntity = getUserEntity();
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findByEmail(Mockito.any())).thenReturn(ofResult);
        when(iUserValidator.HasValidEmail(Mockito.any())).thenReturn(true);
        when(iPasswordValidator.isValid(Mockito.any())).thenReturn(true);

        // Act
        ServiceResult<Boolean> actualForgotPasswordResult = authService.forgotPassword("jane.doe@example.org", "password",
                "Verification Code");

        // Assert
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        verify(iPasswordValidator).isValid(eq("password"));
        verify(iUserValidator).HasValidEmail(eq("jane.doe@example.org"));
        assertEquals("The verification code is incorrect.", actualForgotPasswordResult.getMessageError());
        assertNull(actualForgotPasswordResult.getData());
        assertEquals(HttpStatus.BAD_REQUEST, actualForgotPasswordResult.getHttpStatus());
        assertFalse(actualForgotPasswordResult.isSuccess());
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
    @DisplayName("Test forgotPassword(String, String, String); then throw InvalidCredentialsException")
    
    @MethodsUnderTest({"ServiceResult AuthService.forgotPassword(String, String, String)"})
    void testForgotPassword_thenThrowInvalidCredentialsException() {
        // Arrange
        when(userRepository.findByEmail(Mockito.any()))
                .thenThrow(new InvalidCredentialsException("An error occurred"));
        when(iUserValidator.HasValidEmail(Mockito.any())).thenReturn(true);
        when(iPasswordValidator.isValid(Mockito.any())).thenReturn(true);

        // Act and Assert
        assertThrows(InvalidCredentialsException.class,
                () -> authService.forgotPassword("jane.doe@example.org", "password", "Verification Code"));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        verify(iPasswordValidator).isValid(eq("password"));
        verify(iUserValidator).HasValidEmail(eq("jane.doe@example.org"));
    }

    @Test
    @DisplayName("validateToken - valid token returns true")
    
    @MethodsUnderTest("boolean AuthService.validateToken(String)")
    void testValidateToken_valid() {
        // Arrange
        String token = "jwt-token";
        when(jwtUtil.extractEmail(token)).thenReturn("email@mail.com");
        when(jwtUtil.extractRole(token)).thenReturn("ADMIN");
        when(jwtUtil.validateToken(token, "email@mail.com", "ADMIN")).thenReturn(true);
        when(jwtUtil.isTokenExpired(token)).thenReturn(false);

        // Act
        boolean result = authService.validateToken(token);
        assertTrue(result);
    }

    @Test
    @DisplayName("initiateLogin - valid credentials returns 2FA required")
    
    @MethodsUnderTest("String AuthService.initiateLogin(LoginRequest)")
    void testInitiateLogin_success() {
        // Arrange
        LoginRequest request = new LoginRequest("test@mail.com", "password");
        UserEntity user = new UserEntity();
        user.setEmail("test@mail.com");
        user.setPassword("encodedPass");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPass")).thenReturn(true);

        // Act
        String result = authService.initiateLogin(request);

        // Assert
        assertEquals("Two-factor authentication required.", result);
    }

    @Test
    @DisplayName("loginAdmin - user not admin throws exception")
    
    @MethodsUnderTest("LoginResponse AuthService.loginAdmin(LoginRequest)")
    void testLoginAdmin_notAdmin() {
        // Arrange
        LoginRequest request = new LoginRequest("user@mail.com", "pass");
        UserEntity user = new UserEntity();
        user.setEmail("user@mail.com");
        user.setPassword("encodedPass");
        user.setRole(Role.STUDENT);

        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "encodedPass")).thenReturn(true);

        // Act + Assert
        InvalidCredentialsException ex = assertThrows(InvalidCredentialsException.class, () -> authService.loginAdmin(request));
        assertEquals("User is not an admin.", ex.getMessage());
    }

    @Test
    @DisplayName("verifyTwoFactorCode - valid 2FA returns token")
    
    @MethodsUnderTest("LoginResponse AuthService.verifyTwoFactorCode(TwoFactorRequest)")
    void testVerifyTwoFactorCode_success() {
        // Arrange
        TwoFactorRequest request = new TwoFactorRequest("test@mail.com", 123456);
        UserEntity user = new UserEntity();
        user.setEmail("test@mail.com");
        user.setTwoFactorSecretKey("SECRET");
        user.setRole(Role.ADMIN);

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(google2FAService.verifyTwoFactorCode("SECRET", 123456)).thenReturn(true);
        when(jwtUtil.generateToken("test@mail.com", "ADMIN")).thenReturn("mock-token");

        // Act
        LoginResponse response = authService.verifyTwoFactorCode(request);

        // Assert
        assertEquals("mock-token", response.getToken());
        assertEquals("Authentication successful.", response.getMessage());
    }

    @Test
    @DisplayName("initiateLogin - wrong password throws exception")
    
    @MethodsUnderTest("String AuthService.initiateLogin(LoginRequest)")
    void testInitiateLogin_wrongPassword() {
        // Arrange
        LoginRequest request = new LoginRequest("john.doe@example.org", "wrongPassword");
        UserEntity user = new UserEntity();
        user.setEmail("john.doe@example.org");
        user.setPassword("encodedCorrectPassword");

        when(userRepository.findByEmail("john.doe@example.org")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedCorrectPassword")).thenReturn(false);

        // Act + Assert
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () ->
                authService.initiateLogin(request));

        assertEquals("Invalid email or password.", exception.getMessage());
        verify(userRepository).findByEmail("john.doe@example.org");
        verify(passwordEncoder).matches("wrongPassword", "encodedCorrectPassword");
    }

    @Test
    @DisplayName("verifyTwoFactorCode - wrong 2FA code throws exception")
    
    @MethodsUnderTest("LoginResponse AuthService.verifyTwoFactorCode(TwoFactorRequest)")
    void testVerifyTwoFactorCode_invalidCode() {
        // Arrange
        TwoFactorRequest request = new TwoFactorRequest("john.doe@example.org", 111111);
        UserEntity user = new UserEntity();
        user.setEmail("john.doe@example.org");
        user.setTwoFactorSecretKey("SECRET");

        when(userRepository.findByEmail("john.doe@example.org")).thenReturn(Optional.of(user));
        when(google2FAService.verifyTwoFactorCode("SECRET", 111111)).thenReturn(false);

        // Act + Assert
        TwoFactorAuthenticationException exception = assertThrows(TwoFactorAuthenticationException.class,
                () -> authService.verifyTwoFactorCode(request));

        assertEquals("Invalid two-factor authentication code.", exception.getMessage());
        verify(google2FAService).verifyTwoFactorCode("SECRET", 111111);
    }

}
