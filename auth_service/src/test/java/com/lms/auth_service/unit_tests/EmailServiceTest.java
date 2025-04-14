package com.lms.auth_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.diffblue.cover.annotations.MethodsUnderTest;
import com.lms.auth_service.services.EmailService;
import jakarta.mail.Address;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {EmailService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class EmailServiceTest {
    @Autowired
    private EmailService emailService;

    @MockitoBean
    private JavaMailSender javaMailSender;

    
    @Test
    @DisplayName("Test sendEmail(String, String, String)")
    
    @MethodsUnderTest({"void EmailService.sendEmail(String, String, String)"})
    void testSendEmail() throws MailException {
        // Arrange
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        // Act
        emailService.sendEmail("alice.liddell@example.org", "Hello from the Dreaming Spires",
                "Not all who wander are lost");

        // Assert
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(isA(MimeMessage.class));
    }

    
    @Test
    @DisplayName("Test sendEmail(String, String, String); given JavaMailSender send(MimeMessage) throw RuntimeException(String) with 'foo'")
    
    @MethodsUnderTest({"void EmailService.sendEmail(String, String, String)"})
    void testSendEmail_givenJavaMailSenderSendThrowRuntimeExceptionWithFoo() throws MailException {
        // Arrange
        doThrow(new RuntimeException("foo")).when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> emailService.sendEmail("alice.liddell@example.org",
                "Hello from the Dreaming Spires", "Not all who wander are lost"));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(isA(MimeMessage.class));
    }

    
    @Test
    @DisplayName("Test sendEmail(String, String, String); given MimeMessage setRecipient(RecipientType, Address) does nothing; then calls setRecipient(RecipientType, Address)")
    
    @MethodsUnderTest({"void EmailService.sendEmail(String, String, String)"})
    void testSendEmail_givenMimeMessageSetRecipientDoesNothing_thenCallsSetRecipient()
            throws MessagingException, MailException {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        doNothing().when(mimeMessage).setRecipient(Mockito.any(), Mockito.any());
        doNothing().when(mimeMessage).setContent(Mockito.any());
        doNothing().when(mimeMessage).setSubject(Mockito.any());
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        emailService.sendEmail("alice.liddell@example.org", "Hello from the Dreaming Spires",
                "Not all who wander are lost");

        // Assert
        verify(mimeMessage).setRecipient(isA(RecipientType.class), isA(Address.class));
        verify(mimeMessage).setContent(isA(Multipart.class));
        verify(mimeMessage).setSubject(eq("Hello from the Dreaming Spires"));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(isA(MimeMessage.class));
    }

    
    @Test
    @DisplayName("Test sendEmail(String, String, String); when empty string; then throw RuntimeException")
    
    @MethodsUnderTest({"void EmailService.sendEmail(String, String, String)"})
    void testSendEmail_whenEmptyString_thenThrowRuntimeException() throws MessagingException {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        doNothing().when(mimeMessage).setContent(Mockito.any());
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act and Assert
        assertThrows(RuntimeException.class,
                () -> emailService.sendEmail("", "Hello from the Dreaming Spires", "Not all who wander are lost"));
        verify(mimeMessage).setContent(isA(Multipart.class));
        verify(javaMailSender).createMimeMessage();
    }

   
    @Test
    @DisplayName("Test sendEmail(String, String, String); when 'To'; then calls setRecipient(RecipientType, Address)")
    
    @MethodsUnderTest({"void EmailService.sendEmail(String, String, String)"})
    void testSendEmail_whenTo_thenCallsSetRecipient() throws MessagingException, MailException {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        doNothing().when(mimeMessage).setRecipient(Mockito.any(), Mockito.any());
        doNothing().when(mimeMessage).setContent(Mockito.any());
        doNothing().when(mimeMessage).setSubject(Mockito.any());
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        emailService.sendEmail("To", "Hello from the Dreaming Spires", "Not all who wander are lost");

        // Assert
        verify(mimeMessage).setRecipient(isA(RecipientType.class), isA(Address.class));
        verify(mimeMessage).setContent(isA(Multipart.class));
        verify(mimeMessage).setSubject(eq("Hello from the Dreaming Spires"));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(isA(MimeMessage.class));
    }

    
    @Test
    @DisplayName("Test sendEmailWithQRCode(String, String, String, String); when empty string; then throw RuntimeException")
    
    @MethodsUnderTest({"void EmailService.sendEmailWithQRCode(String, String, String, String)"})
    void testSendEmailWithQRCode_whenEmptyString_thenThrowRuntimeException() throws MessagingException {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        doNothing().when(mimeMessage).setContent(Mockito.any());
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> emailService.sendEmailWithQRCode("", "Hello from the Dreaming Spires",
                "password", "https://example.org/example"));
        verify(mimeMessage).setContent(isA(Multipart.class));
        verify(javaMailSender).createMimeMessage();
    }

   
    @Test
    @DisplayName("Test sendVerificationCodeEmail(String)")
    
    @MethodsUnderTest({"String EmailService.sendVerificationCodeEmail(String)"})
    void testSendVerificationCodeEmail() throws MailException {
        // Arrange
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        // Act
        emailService.sendVerificationCodeEmail("jane.doe@example.org");

        // Assert
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(isA(MimeMessage.class));
    }

    
    @Test
    @DisplayName("Test sendVerificationCodeEmail(String)")
    
    @MethodsUnderTest({"String EmailService.sendVerificationCodeEmail(String)"})
    void testSendVerificationCodeEmail2() throws MailException {
        // Arrange
        doThrow(new RuntimeException("Password Reset Verification Code")).when(javaMailSender)
                .send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> emailService.sendVerificationCodeEmail("jane.doe@example.org"));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(isA(MimeMessage.class));
    }

    
    @Test
    @DisplayName("Test sendVerificationCodeEmail(String); then calls setRecipient(RecipientType, Address)")
    
    @MethodsUnderTest({"String EmailService.sendVerificationCodeEmail(String)"})
    void testSendVerificationCodeEmail_thenCallsSetRecipient() throws MessagingException, MailException {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        doNothing().when(mimeMessage).setRecipient(Mockito.any(), Mockito.any());
        doNothing().when(mimeMessage).setContent(Mockito.any());
        doNothing().when(mimeMessage).setSubject(Mockito.any());
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        emailService.sendVerificationCodeEmail("jane.doe@example.org");

        // Assert
        verify(mimeMessage).setRecipient(isA(RecipientType.class), isA(Address.class));
        verify(mimeMessage).setContent(isA(Multipart.class));
        verify(mimeMessage).setSubject(eq("Password Reset Verification Code"));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(isA(MimeMessage.class));
    }

    
    @Test
    @DisplayName("Test sendVerificationCodeEmail(String); when '%06d'; then calls setRecipient(RecipientType, Address)")
    
    @MethodsUnderTest({"String EmailService.sendVerificationCodeEmail(String)"})
    void testSendVerificationCodeEmail_when06d_thenCallsSetRecipient() throws MessagingException, MailException {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        doNothing().when(mimeMessage).setRecipient(Mockito.any(), Mockito.any());
        doNothing().when(mimeMessage).setContent(Mockito.any());
        doNothing().when(mimeMessage).setSubject(Mockito.any());
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        emailService.sendVerificationCodeEmail("%06d");

        // Assert
        verify(mimeMessage).setRecipient(isA(RecipientType.class), isA(Address.class));
        verify(mimeMessage).setContent(isA(Multipart.class));
        verify(mimeMessage).setSubject(eq("Password Reset Verification Code"));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(isA(MimeMessage.class));
    }

   
    @Test
    @DisplayName("Test sendVerificationCodeEmail(String); when empty string; then throw RuntimeException")
    
    @MethodsUnderTest({"String EmailService.sendVerificationCodeEmail(String)"})
    void testSendVerificationCodeEmail_whenEmptyString_thenThrowRuntimeException() throws MessagingException {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        doNothing().when(mimeMessage).setContent(Mockito.any());
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> emailService.sendVerificationCodeEmail(""));
        verify(mimeMessage).setContent(isA(Multipart.class));
        verify(javaMailSender).createMimeMessage();
    }

   
    @Test
    @DisplayName("Test sendVerificationCodeEmail(String); when 'jane.doe@example.orgPassword Reset Verification Code'")
    
    @MethodsUnderTest({"String EmailService.sendVerificationCodeEmail(String)"})
    void testSendVerificationCodeEmail_whenJaneDoeExampleOrgPasswordResetVerificationCode() throws MessagingException {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        doNothing().when(mimeMessage).setContent(Mockito.any());
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act and Assert
        assertThrows(RuntimeException.class,
                () -> emailService.sendVerificationCodeEmail("jane.doe@example.orgPassword Reset Verification Code"));
        verify(mimeMessage).setContent(isA(Multipart.class));
        verify(javaMailSender).createMimeMessage();
    }

    
    @Test
    @DisplayName("Test sendVerificationCodeEmail(String); when 'jane.doe@example.orgjane.doe@example.org'")
    
    @MethodsUnderTest({"String EmailService.sendVerificationCodeEmail(String)"})
    void testSendVerificationCodeEmail_whenJaneDoeExampleOrgjaneDoeExampleOrg() throws MessagingException {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        doNothing().when(mimeMessage).setContent(Mockito.any());
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act and Assert
        assertThrows(RuntimeException.class,
                () -> emailService.sendVerificationCodeEmail("jane.doe@example.orgjane.doe@example.org"));
        verify(mimeMessage).setContent(isA(Multipart.class));
        verify(javaMailSender).createMimeMessage();
    }

    
    @Test
    @DisplayName("Test sendVerificationCodeEmail(String); when 'Password Reset Verification Code'")
    
    @MethodsUnderTest({"String EmailService.sendVerificationCodeEmail(String)"})
    void testSendVerificationCodeEmail_whenPasswordResetVerificationCode() throws MessagingException {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        doNothing().when(mimeMessage).setContent(Mockito.any());
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act and Assert
        assertThrows(RuntimeException.class,
                () -> emailService.sendVerificationCodeEmail("Password Reset Verification Code"));
        verify(mimeMessage).setContent(isA(Multipart.class));
        verify(javaMailSender).createMimeMessage();
    }
}
