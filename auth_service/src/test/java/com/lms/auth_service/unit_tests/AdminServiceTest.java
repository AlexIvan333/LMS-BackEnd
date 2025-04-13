package com.lms.auth_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.diffblue.cover.annotations.MethodsUnderTest;
import com.lms.auth_service.dtos.requests.CreateUserRequest;
import com.lms.auth_service.dtos.responses.AdminResponse;
import com.lms.auth_service.dtos.responses.ServiceResult;
import com.lms.auth_service.entities.enums.Role;
import com.lms.auth_service.entities.relational.AdminEntity;
import com.lms.auth_service.repositories.relational.AdminRepository;
import com.lms.auth_service.services.AdminService;
import com.lms.auth_service.validation.intrefaces.IUserValidator;
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

@ContextConfiguration(classes = {AdminService.class, PasswordEncoder.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AdminServiceTest {
    @MockitoBean
    private AdminRepository adminRepository;

    @Autowired
    private AdminService adminService;

    @MockitoBean
    private IUserValidator iUserValidator;

    @MockitoBean
    private PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("Test createAdmin(CreateUserRequest)")
    
    @MethodsUnderTest({"ServiceResult AdminService.createAdmin(CreateUserRequest)"})
    void testCreateAdmin() {
        // Arrange
        when(iUserValidator.HasValidEmail(Mockito.any())).thenReturn(true);

        // Act
        ServiceResult<AdminResponse> actualCreateAdminResult = adminService.createAdmin(new CreateUserRequest());

        // Assert
        verify(iUserValidator).HasValidEmail(isNull());
        assertEquals("There already is an account with this email address.", actualCreateAdminResult.getMessageError());
        assertNull(actualCreateAdminResult.getData());
        assertEquals(HttpStatus.BAD_REQUEST, actualCreateAdminResult.getHttpStatus());
        assertFalse(actualCreateAdminResult.isSuccess());
    }

    
    @Test
    @DisplayName("Test createAdmin(CreateUserRequest); given AdminEntity() Active is 'true'; then return Data Id is 'null'")
    
    @MethodsUnderTest({"ServiceResult AdminService.createAdmin(CreateUserRequest)"})
    void testCreateAdmin_givenAdminEntityActiveIsTrue_thenReturnDataIdIsNull() {
        // Arrange
        var adminEntity = getAdminEntity();
        when(adminRepository.save(Mockito.any())).thenReturn(adminEntity);
        when(iUserValidator.HasValidEmail(Mockito.any())).thenReturn(false);

        CreateUserRequest request = new CreateUserRequest();
        request.setStreetNumber(10);

        // Act
        ServiceResult<AdminResponse> actualCreateAdminResult = adminService.createAdmin(request);

        // Assert
        verify(iUserValidator).HasValidEmail(isNull());
        verify(adminRepository).save(isA(AdminEntity.class));
        AdminResponse data = actualCreateAdminResult.getData();
        assertNull(data.getId());
        assertNull(data.getEmail());
        assertNull(actualCreateAdminResult.getMessageError());
        assertEquals(Role.ADMIN, data.getRole());
        assertEquals(HttpStatus.CREATED, actualCreateAdminResult.getHttpStatus());
        assertTrue(actualCreateAdminResult.isSuccess());
    }

    private static AdminEntity getAdminEntity() {
        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setActive(true);
        adminEntity.setCity("Oxford");
        adminEntity.setCodeForgotPassword("password");
        adminEntity.setCountry("GB");
        adminEntity.setDepartment("Department");
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
        return adminEntity;
    }
}
