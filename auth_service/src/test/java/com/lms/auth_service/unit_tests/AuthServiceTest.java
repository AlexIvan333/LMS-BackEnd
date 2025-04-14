package com.lms.auth_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
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
import com.lms.auth_service.validation.UserValidator;
import com.lms.auth_service.validation.intrefaces.IPasswordValidator;
import com.lms.auth_service.validation.intrefaces.IUserValidator;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuthService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
@ExtendWith(MockitoExtension.class)
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
    @DisplayName("Test initiateLogin(LoginRequest)")
    
    @MethodsUnderTest({"String AuthService.initiateLogin(LoginRequest)"})
    void testInitiateLogin() throws InvalidCredentialsException {
        // Arrange
        var userEntity = getUserEntity();
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findByEmail(Mockito.any())).thenReturn(ofResult);
        when(passwordEncoder.matches(Mockito.any(), Mockito.any()))
                .thenThrow(new InvalidCredentialsException("An error occurred"));

        // Act and Assert
        assertThrows(InvalidCredentialsException.class,
                () -> authService.initiateLogin(new LoginRequest("jane.doe@example.org", "password")));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        verify(passwordEncoder).matches(isA(CharSequence.class), eq("password"));
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
    @DisplayName("Test initiateLogin(LoginRequest)")
    
    @MethodsUnderTest({"String AuthService.initiateLogin(LoginRequest)"})
    void testInitiateLogin2() throws InvalidCredentialsException {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail(Mockito.any()))
                .thenThrow(new InvalidCredentialsException("An error occurred"));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        JwtUtil jwtUtil = new JwtUtil();
        UserValidator userValidation = new UserValidator(mock(UserRepository.class));
        IPasswordValidator passwordValidator = mock(IPasswordValidator.class);
        Google2FAService google2FAService = new Google2FAService();
        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtUtil, userValidation,
                passwordValidator, google2FAService, new EmailService(new JavaMailSenderImpl()));

        // Act and Assert
        assertThrows(InvalidCredentialsException.class,
                () -> authService.initiateLogin(new LoginRequest("jane.doe@example.org", "password")));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
    }


    @Test
    @DisplayName("Test initiateLogin(LoginRequest); given PasswordEncoder matches(CharSequence, String) return 'false'; then calls matches(CharSequence, String)")
    
    @MethodsUnderTest({"String AuthService.initiateLogin(LoginRequest)"})
    void testInitiateLogin_givenPasswordEncoderMatchesReturnFalse_thenCallsMatches() throws InvalidCredentialsException {
        // Arrange
        var userEntity = getUserEntity();
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findByEmail(Mockito.any())).thenReturn(ofResult);
        when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(false);

        // Act and Assert
        assertThrows(InvalidCredentialsException.class,
                () -> authService.initiateLogin(new LoginRequest("jane.doe@example.org", "password")));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        verify(passwordEncoder).matches(isA(CharSequence.class), eq("password"));
    }


    @Test
    @DisplayName("Test initiateLogin(LoginRequest); given UserRepository findByEmail(String) return empty")
    
    @MethodsUnderTest({"String AuthService.initiateLogin(LoginRequest)"})
    void testInitiateLogin_givenUserRepositoryFindByEmailReturnEmpty() throws InvalidCredentialsException {
        // Arrange
        Optional<UserEntity> emptyResult = Optional.empty();
        when(userRepository.findByEmail(Mockito.any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(InvalidCredentialsException.class,
                () -> authService.initiateLogin(new LoginRequest("jane.doe@example.org", "password")));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
    }


    @Test
    @DisplayName("Test initiateLogin(LoginRequest); then return 'Two-factor authentication required.'")
    
    @MethodsUnderTest({"String AuthService.initiateLogin(LoginRequest)"})
    void testInitiateLogin_thenReturnTwoFactorAuthenticationRequired() throws InvalidCredentialsException {
        // Arrange
        var userEntity = getUserEntity();
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findByEmail(Mockito.any())).thenReturn(ofResult);
        when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);

        // Act
        String actualInitiateLoginResult = authService.initiateLogin(new LoginRequest("jane.doe@example.org", "password"));

        // Assert
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        verify(passwordEncoder).matches(isA(CharSequence.class), eq("password"));
        assertEquals("Two-factor authentication required.", actualInitiateLoginResult);
    }


    @Test
    @DisplayName("Test loginAdmin(LoginRequest)")
    
    @MethodsUnderTest({"LoginResponse AuthService.loginAdmin(LoginRequest)"})
    void testLoginAdmin() throws InvalidCredentialsException {
        // Arrange
        var userEntity = getUserEntity();
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findByEmail(Mockito.any())).thenReturn(ofResult);
        when(passwordEncoder.matches(Mockito.any(), Mockito.any()))
                .thenThrow(new InvalidCredentialsException("An error occurred"));

        // Act and Assert
        assertThrows(InvalidCredentialsException.class,
                () -> authService.loginAdmin(new LoginRequest("jane.doe@example.org", "password")));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        verify(passwordEncoder).matches(isA(CharSequence.class), eq("password"));
    }


    @Test
    @DisplayName("Test loginAdmin(LoginRequest)")
    
    @MethodsUnderTest({"LoginResponse AuthService.loginAdmin(LoginRequest)"})
    void testLoginAdmin2() throws InvalidCredentialsException {
        // Arrange
        AdminEntity adminEntity = mock(AdminEntity.class);
        when(adminEntity.getEmail()).thenReturn("jane.doe@example.org");
        when(adminEntity.getRole()).thenReturn(Role.ADMIN);
        when(adminEntity.getPassword()).thenReturn("password");
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
        when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
        when(jwtUtil.generateToken(Mockito.any(), Mockito.any()))
                .thenThrow(new InvalidCredentialsException("An error occurred"));

        // Act and Assert
        assertThrows(InvalidCredentialsException.class,
                () -> authService.loginAdmin(new LoginRequest("jane.doe@example.org", "password")));
        verify(jwtUtil).generateToken(eq("jane.doe@example.org"), eq("ADMIN"));
        verify(adminEntity).getEmail();
        verify(adminEntity).getPassword();
        verify(adminEntity, atLeast(1)).getRole();
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
        verify(passwordEncoder).matches(isA(CharSequence.class), eq("password"));
    }


    @Test
    @DisplayName("Test loginAdmin(LoginRequest)")
    
    @MethodsUnderTest({"LoginResponse AuthService.loginAdmin(LoginRequest)"})
    void testLoginAdmin3() throws InvalidCredentialsException {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail(Mockito.any()))
                .thenThrow(new InvalidCredentialsException("An error occurred"));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        JwtUtil jwtUtil = new JwtUtil();
        UserValidator userValidation = new UserValidator(mock(UserRepository.class));
        IPasswordValidator passwordValidator = mock(IPasswordValidator.class);
        Google2FAService google2FAService = new Google2FAService();
        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtUtil, userValidation,
                passwordValidator, google2FAService, new EmailService(new JavaMailSenderImpl()));

        // Act and Assert
        assertThrows(InvalidCredentialsException.class,
                () -> authService.loginAdmin(new LoginRequest("jane.doe@example.org", "password")));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
    }

    @Test
    @DisplayName("Test loginAdmin(LoginRequest); given JwtUtil generateToken(String, String) return 'ABC123'; then return Token is 'ABC123'")
    
    @MethodsUnderTest({"LoginResponse AuthService.loginAdmin(LoginRequest)"})
    void testLoginAdmin_givenJwtUtilGenerateTokenReturnAbc123_thenReturnTokenIsAbc123()
            throws InvalidCredentialsException {
        // Arrange
        AdminEntity adminEntity = mock(AdminEntity.class);
        when(adminEntity.getEmail()).thenReturn("jane.doe@example.org");
        when(adminEntity.getRole()).thenReturn(Role.ADMIN);
        when(adminEntity.getPassword()).thenReturn("password");
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
        when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
        when(jwtUtil.generateToken(Mockito.any(), Mockito.any())).thenReturn("ABC123");

        // Act
        LoginResponse actualLoginAdminResult = authService.loginAdmin(new LoginRequest("jane.doe@example.org", "password"));

        // Assert
        verify(jwtUtil).generateToken(eq("jane.doe@example.org"), eq("ADMIN"));
        verify(adminEntity).getEmail();
        verify(adminEntity).getPassword();
        verify(adminEntity, atLeast(1)).getRole();
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
        verify(passwordEncoder).matches(isA(CharSequence.class), eq("password"));
        assertEquals("ABC123", actualLoginAdminResult.getToken());
        assertEquals("Authentication successful.", actualLoginAdminResult.getMessage());
    }


    @Test
    @DisplayName("Test loginAdmin(LoginRequest); given PasswordEncoder matches(CharSequence, String) return 'false'; then calls getPassword()")
    
    @MethodsUnderTest({"LoginResponse AuthService.loginAdmin(LoginRequest)"})
    void testLoginAdmin_givenPasswordEncoderMatchesReturnFalse_thenCallsGetPassword() throws InvalidCredentialsException {
        // Arrange
        AdminEntity adminEntity = mock(AdminEntity.class);
        when(adminEntity.getPassword()).thenReturn("password");
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
        when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(false);

        // Act and Assert
        assertThrows(InvalidCredentialsException.class,
                () -> authService.loginAdmin(new LoginRequest("jane.doe@example.org", "password")));
        verify(adminEntity).getPassword();
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
        verify(passwordEncoder).matches(isA(CharSequence.class), eq("password"));
    }


    @Test
    @DisplayName("Test loginAdmin(LoginRequest); given UserEntity() Active is 'true'; then calls matches(CharSequence, String)")
    
    @MethodsUnderTest({"LoginResponse AuthService.loginAdmin(LoginRequest)"})
    void testLoginAdmin_givenUserEntityActiveIsTrue_thenCallsMatches() throws InvalidCredentialsException {
        // Arrange
        var userEntity = getUserEntity();
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findByEmail(Mockito.any())).thenReturn(ofResult);
        when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);

        // Act and Assert
        assertThrows(InvalidCredentialsException.class,
                () -> authService.loginAdmin(new LoginRequest("jane.doe@example.org", "password")));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        verify(passwordEncoder).matches(isA(CharSequence.class), eq("password"));
    }


    @Test
    @DisplayName("Test loginAdmin(LoginRequest); given UserRepository findByEmail(String) return empty")
    
    @MethodsUnderTest({"LoginResponse AuthService.loginAdmin(LoginRequest)"})
    void testLoginAdmin_givenUserRepositoryFindByEmailReturnEmpty() throws InvalidCredentialsException {
        // Arrange
        Optional<UserEntity> emptyResult = Optional.empty();
        when(userRepository.findByEmail(Mockito.any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(InvalidCredentialsException.class,
                () -> authService.loginAdmin(new LoginRequest("jane.doe@example.org", "password")));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
    }


    @Test
    @DisplayName("Test verifyTwoFactorCode(TwoFactorRequest)")
    
    @MethodsUnderTest({"LoginResponse AuthService.verifyTwoFactorCode(TwoFactorRequest)"})
    void testVerifyTwoFactorCode() {
        // Arrange
        var userEntity = getUserEntity();
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail(Mockito.any())).thenReturn(ofResult);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        JwtUtil jwtUtil = new JwtUtil();
        UserValidator userValidation = new UserValidator(mock(UserRepository.class));
        IPasswordValidator passwordValidator = mock(IPasswordValidator.class);
        Google2FAService google2FAService = new Google2FAService();
        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtUtil, userValidation,
                passwordValidator, google2FAService, new EmailService(new JavaMailSenderImpl()));

        // Act and Assert
        assertThrows(TwoFactorAuthenticationException.class,
                () -> authService.verifyTwoFactorCode(new TwoFactorRequest("jane.doe@example.org", 1)));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
    }


    @Test
    @DisplayName("Test verifyTwoFactorCode(TwoFactorRequest); given Google2FAService verifyTwoFactorCode(String, int) return 'false'")
    
    @MethodsUnderTest({"LoginResponse AuthService.verifyTwoFactorCode(TwoFactorRequest)"})
    void testVerifyTwoFactorCode_givenGoogle2FAServiceVerifyTwoFactorCodeReturnFalse() {
        // Arrange
        AdminEntity adminEntity = mock(AdminEntity.class);
        when(adminEntity.getTwoFactorSecretKey()).thenReturn("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
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
        when(google2FAService.verifyTwoFactorCode(Mockito.any(), anyInt())).thenReturn(false);

        // Act and Assert
        assertThrows(TwoFactorAuthenticationException.class,
                () -> authService.verifyTwoFactorCode(new TwoFactorRequest("jane.doe@example.org", 1)));
        verify(adminEntity).getTwoFactorSecretKey();
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
        verify(google2FAService).verifyTwoFactorCode(eq("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY"), eq(1));
    }


    @Test
    @DisplayName("Test verifyTwoFactorCode(TwoFactorRequest); given UserRepository findByEmail(String) return empty")
    
    @MethodsUnderTest({"LoginResponse AuthService.verifyTwoFactorCode(TwoFactorRequest)"})
    void testVerifyTwoFactorCode_givenUserRepositoryFindByEmailReturnEmpty() {
        // Arrange
        Optional<UserEntity> emptyResult = Optional.empty();
        when(userRepository.findByEmail(Mockito.any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(TwoFactorAuthenticationException.class,
                () -> authService.verifyTwoFactorCode(new TwoFactorRequest("jane.doe@example.org", 1)));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
    }


    @Test
    @DisplayName("Test verifyTwoFactorCode(TwoFactorRequest); then calls getEmail()")
    
    @MethodsUnderTest({"LoginResponse AuthService.verifyTwoFactorCode(TwoFactorRequest)"})
    void testVerifyTwoFactorCode_thenCallsGetEmail() {
        // Arrange
        AdminEntity adminEntity = mock(AdminEntity.class);
        when(adminEntity.getRole()).thenReturn(Role.STUDENT);
        when(adminEntity.getEmail()).thenReturn("jane.doe@example.org");
        when(adminEntity.getTwoFactorSecretKey()).thenReturn("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
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
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail(Mockito.any())).thenReturn(ofResult);
        Google2FAService google2FAService = mock(Google2FAService.class);
        when(google2FAService.verifyTwoFactorCode(Mockito.any(), anyInt())).thenReturn(true);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        JwtUtil jwtUtil = new JwtUtil();
        UserValidator userValidation = new UserValidator(mock(UserRepository.class));
        IPasswordValidator passwordValidator = mock(IPasswordValidator.class);
        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtUtil, userValidation,
                passwordValidator, google2FAService, new EmailService(new JavaMailSenderImpl()));

        // Act
        LoginResponse actualVerifyTwoFactorCodeResult = authService
                .verifyTwoFactorCode(new TwoFactorRequest("jane.doe@example.org", 1));

        // Assert
        verify(adminEntity).getEmail();
        verify(adminEntity).getRole();
        verify(adminEntity).getTwoFactorSecretKey();
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
        verify(google2FAService).verifyTwoFactorCode(eq("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY"), eq(1));
        assertEquals("Authentication successful.", actualVerifyTwoFactorCodeResult.getMessage());
    }


    @Test
    @DisplayName("Test verifyTwoFactorCode(TwoFactorRequest); then return Token is 'ABC123'")
    
    @MethodsUnderTest({"LoginResponse AuthService.verifyTwoFactorCode(TwoFactorRequest)"})
    void testVerifyTwoFactorCode_thenReturnTokenIsAbc123() {
        // Arrange
        var userEntity = getUserEntity();
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findByEmail(Mockito.any())).thenReturn(ofResult);
        when(jwtUtil.generateToken(Mockito.any(), Mockito.any())).thenReturn("ABC123");
        when(google2FAService.verifyTwoFactorCode(Mockito.any(), anyInt())).thenReturn(true);

        // Act
        LoginResponse actualVerifyTwoFactorCodeResult = authService
                .verifyTwoFactorCode(new TwoFactorRequest("jane.doe@example.org", 1));

        // Assert
        verify(jwtUtil).generateToken(eq("jane.doe@example.org"), eq("STUDENT"));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        verify(google2FAService).verifyTwoFactorCode(eq("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY"), eq(1));
        assertEquals("ABC123", actualVerifyTwoFactorCodeResult.getToken());
        assertEquals("Authentication successful.", actualVerifyTwoFactorCodeResult.getMessage());
    }


    @Test
    @DisplayName("Test verifyTwoFactorCode(TwoFactorRequest); then throw InvalidCredentialsException")
    
    @MethodsUnderTest({"LoginResponse AuthService.verifyTwoFactorCode(TwoFactorRequest)"})
    void testVerifyTwoFactorCode_thenThrowInvalidCredentialsException() {
        // Arrange
        var userEntity = getUserEntity();
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findByEmail(Mockito.any())).thenReturn(ofResult);
        when(jwtUtil.generateToken(Mockito.any(), Mockito.any()))
                .thenThrow(new InvalidCredentialsException("An error occurred"));
        when(google2FAService.verifyTwoFactorCode(Mockito.any(), anyInt())).thenReturn(true);

        // Act and Assert
        assertThrows(InvalidCredentialsException.class,
                () -> authService.verifyTwoFactorCode(new TwoFactorRequest("jane.doe@example.org", 1)));
        verify(jwtUtil).generateToken(eq("jane.doe@example.org"), eq("STUDENT"));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        verify(google2FAService).verifyTwoFactorCode(eq("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY"), eq(1));
    }


    @Test
    @DisplayName("Test validateToken(String)")
    
    @MethodsUnderTest({"boolean AuthService.validateToken(String)"})
    void testValidateToken() {
        // Arrange
        when(jwtUtil.extractEmail(Mockito.any()))
                .thenThrow(new TwoFactorAuthenticationException("An error occurred"));

        // Act
        boolean actualValidateTokenResult = authService.validateToken("ABC123");

        // Assert
        verify(jwtUtil).extractEmail(eq("ABC123"));
        assertFalse(actualValidateTokenResult);
    }


    @Test
    @DisplayName("Test validateToken(String); given JwtUtil isTokenExpired(String) return 'false'; then return 'true'")
    
    @MethodsUnderTest({"boolean AuthService.validateToken(String)"})
    void testValidateToken_givenJwtUtilIsTokenExpiredReturnFalse_thenReturnTrue() {
        // Arrange
        when(jwtUtil.isTokenExpired(Mockito.any())).thenReturn(false);
        when(jwtUtil.validateToken(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
        when(jwtUtil.extractEmail(Mockito.any())).thenReturn("jane.doe@example.org");
        when(jwtUtil.extractRole(Mockito.any())).thenReturn("Extract Role");

        // Act
        boolean actualValidateTokenResult = authService.validateToken("ABC123");

        // Assert
        verify(jwtUtil).extractEmail(eq("ABC123"));
        verify(jwtUtil).extractRole(eq("ABC123"));
        verify(jwtUtil).isTokenExpired(eq("ABC123"));
        verify(jwtUtil).validateToken(eq("ABC123"), eq("jane.doe@example.org"), eq("Extract Role"));
        assertTrue(actualValidateTokenResult);
    }


    @Test
    @DisplayName("Test validateToken(String); given JwtUtil isTokenExpired(String) return 'true'; then calls extractRole(String)")
    
    @MethodsUnderTest({"boolean AuthService.validateToken(String)"})
    void testValidateToken_givenJwtUtilIsTokenExpiredReturnTrue_thenCallsExtractRole() {
        // Arrange
        when(jwtUtil.isTokenExpired(Mockito.any())).thenReturn(true);
        when(jwtUtil.validateToken(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
        when(jwtUtil.extractEmail(Mockito.any())).thenReturn("jane.doe@example.org");
        when(jwtUtil.extractRole(Mockito.any())).thenReturn("Extract Role");

        // Act
        boolean actualValidateTokenResult = authService.validateToken("ABC123");

        // Assert
        verify(jwtUtil).extractEmail(eq("ABC123"));
        verify(jwtUtil).extractRole(eq("ABC123"));
        verify(jwtUtil).isTokenExpired(eq("ABC123"));
        verify(jwtUtil).validateToken(eq("ABC123"), eq("jane.doe@example.org"), eq("Extract Role"));
        assertFalse(actualValidateTokenResult);
    }


    @Test
    @DisplayName("Test validateToken(String); given JwtUtil validateToken(String, String, String) return 'false'; then calls extractRole(String)")
    
    @MethodsUnderTest({"boolean AuthService.validateToken(String)"})
    void testValidateToken_givenJwtUtilValidateTokenReturnFalse_thenCallsExtractRole() {
        // Arrange
        when(jwtUtil.isTokenExpired(Mockito.any())).thenReturn(true);
        when(jwtUtil.validateToken(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
        when(jwtUtil.extractEmail(Mockito.any())).thenReturn("jane.doe@example.org");
        when(jwtUtil.extractRole(Mockito.any())).thenReturn("Extract Role");

        // Act
        boolean actualValidateTokenResult = authService.validateToken("ABC123");

        // Assert
        verify(jwtUtil).extractEmail(eq("ABC123"));
        verify(jwtUtil).extractRole(eq("ABC123"));
        verify(jwtUtil).isTokenExpired(eq("ABC123"));
        verify(jwtUtil).validateToken(eq("ABC123"), eq("jane.doe@example.org"), eq("Extract Role"));
        assertFalse(actualValidateTokenResult);
    }


    @Test
    @DisplayName("Test validateToken(String); given UserValidator(UserRepository) with UserRepository; then return 'false'")
    
    @MethodsUnderTest({"boolean AuthService.validateToken(String)"})
    void testValidateToken_givenUserValidatorWithUserRepository_thenReturnFalse() {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        JwtUtil jwtUtil = new JwtUtil();
        UserValidator userValidation = new UserValidator(mock(UserRepository.class));
        IPasswordValidator passwordValidator = mock(IPasswordValidator.class);
        Google2FAService google2FAService = new Google2FAService();

        // Act and Assert
        assertFalse((new AuthService(userRepository, passwordEncoder, jwtUtil, userValidation, passwordValidator,
                google2FAService, new EmailService(new JavaMailSenderImpl()))).validateToken("ABC123"));
    }


    @Test
    @DisplayName("Test sendPasswordResetEmailVerification(String)")
    
    @MethodsUnderTest({"ServiceResult AuthService.sendPasswordResetEmailVerification(String)"})
    void testSendPasswordResetEmailVerification() {
        // Arrange
        var userEntity = getUserEntity();
        Optional<UserEntity> ofResult = Optional.of(userEntity);

        var userEntity2 = getUserEntity();
        when(userRepository.save(Mockito.any())).thenReturn(userEntity2);
        when(userRepository.findByEmail(Mockito.any())).thenReturn(ofResult);
        when(emailService.sendVerificationCodeEmail(Mockito.any())).thenReturn("jane.doe@example.org");

        // Act
        ServiceResult<Boolean> actualSendPasswordResetEmailVerificationResult = authService
                .sendPasswordResetEmailVerification("jane.doe@example.org");

        // Assert
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        verify(emailService).sendVerificationCodeEmail(eq("jane.doe@example.org"));
        verify(userRepository).save(isA(UserEntity.class));
        assertEquals("Email send successfully.", actualSendPasswordResetEmailVerificationResult.getMessageError());
        assertEquals(HttpStatus.OK, actualSendPasswordResetEmailVerificationResult.getHttpStatus());
        assertTrue(actualSendPasswordResetEmailVerificationResult.getData());
        assertTrue(actualSendPasswordResetEmailVerificationResult.isSuccess());
    }


    @Test
    @DisplayName("Test sendPasswordResetEmailVerification(String)")
    
    @MethodsUnderTest({"ServiceResult AuthService.sendPasswordResetEmailVerification(String)"})
    void testSendPasswordResetEmailVerification2() {
        // Arrange
        var userEntity = getUserEntity();
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.save(Mockito.any()))
                .thenThrow(new InvalidCredentialsException("An error occurred"));
        when(userRepository.findByEmail(Mockito.any())).thenReturn(ofResult);
        when(emailService.sendVerificationCodeEmail(Mockito.any())).thenReturn("jane.doe@example.org");

        // Act and Assert
        assertThrows(InvalidCredentialsException.class,
                () -> authService.sendPasswordResetEmailVerification("jane.doe@example.org"));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        verify(emailService).sendVerificationCodeEmail(eq("jane.doe@example.org"));
        verify(userRepository).save(isA(UserEntity.class));
    }


    @Test
    @DisplayName("Test sendPasswordResetEmailVerification(String)")
    
    @MethodsUnderTest({"ServiceResult AuthService.sendPasswordResetEmailVerification(String)"})
    void testSendPasswordResetEmailVerification3() {
        // Arrange
        when(userRepository.findByEmail(Mockito.any()))
                .thenThrow(new InvalidCredentialsException("An error occurred"));
        when(emailService.sendVerificationCodeEmail(Mockito.any())).thenReturn("jane.doe@example.org");

        // Act and Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.sendPasswordResetEmailVerification(null));
        verify(userRepository).findByEmail(isNull());
        verify(emailService).sendVerificationCodeEmail(isNull());
    }


    @Test
    @DisplayName("Test sendPasswordResetEmailVerification(String); then calls send(MimeMessage)")
    
    @MethodsUnderTest({"ServiceResult AuthService.sendPasswordResetEmailVerification(String)"})
    void testSendPasswordResetEmailVerification_thenCallsSend() throws MailException {
        // Arrange
        var userEntity = getUserEntity();
        Optional<UserEntity> ofResult = Optional.of(userEntity);

        var userEntity2 = getUserEntity();
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(Mockito.any())).thenReturn(userEntity2);
        when(userRepository.findByEmail(Mockito.any())).thenReturn(ofResult);
        JavaMailSenderImpl mailSender = mock(JavaMailSenderImpl.class);
        doNothing().when(mailSender).send(Mockito.<MimeMessage>any());
        when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        EmailService emailService = new EmailService(mailSender);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        JwtUtil jwtUtil = new JwtUtil();
        UserValidator userValidation = new UserValidator(mock(UserRepository.class));
        IPasswordValidator passwordValidator = mock(IPasswordValidator.class);

        // Act
        ServiceResult<Boolean> actualSendPasswordResetEmailVerificationResult = (new AuthService(userRepository,
                passwordEncoder, jwtUtil, userValidation, passwordValidator, new Google2FAService(), emailService))
                .sendPasswordResetEmailVerification("jane.doe@example.org");

        // Assert
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        verify(userRepository).save(isA(UserEntity.class));
        verify(mailSender).send(isA(MimeMessage.class));
        verify(mailSender).createMimeMessage();
        assertEquals("Email send successfully.", actualSendPasswordResetEmailVerificationResult.getMessageError());
        assertEquals(HttpStatus.OK, actualSendPasswordResetEmailVerificationResult.getHttpStatus());
        assertTrue(actualSendPasswordResetEmailVerificationResult.getData());
        assertTrue(actualSendPasswordResetEmailVerificationResult.isSuccess());
    }


    @Test
    @DisplayName("Test forgotPassword(String, String, String)")
    
    @MethodsUnderTest({"ServiceResult AuthService.forgotPassword(String, String, String)"})
    void testForgotPassword() {
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
    void testForgotPassword3() {
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
}
