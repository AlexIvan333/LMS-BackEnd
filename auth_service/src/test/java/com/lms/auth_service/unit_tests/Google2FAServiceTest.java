package com.lms.auth_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertFalse;

import com.diffblue.cover.annotations.MethodsUnderTest;
import com.lms.auth_service.services.Google2FAService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {Google2FAService.class})
@ExtendWith(SpringExtension.class)
class Google2FAServiceTest {
    @Autowired
    private Google2FAService google2FAService;

    
    @Test
    @DisplayName("Test verifyTwoFactorCode(String, int); then return 'false'")
    
    @MethodsUnderTest({"boolean Google2FAService.verifyTwoFactorCode(String, int)"})
    void testVerifyTwoFactorCode_thenReturnFalse() {
        // Arrange, Act and Assert
        assertFalse(google2FAService.verifyTwoFactorCode("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY", 1));
    }


    @Test
    @DisplayName("Test verifyTwoFactorCode(String, int); when zero; then return 'false'")
    
    @MethodsUnderTest({"boolean Google2FAService.verifyTwoFactorCode(String, int)"})
    void testVerifyTwoFactorCode_whenZero_thenReturnFalse() {
        // Arrange, Act and Assert
        assertFalse(google2FAService.verifyTwoFactorCode("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY", 0));
    }
}
