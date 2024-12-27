package com.lms.backend;

import com.lms.backend.services.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.verify;


@SpringBootTest
public class EmailServiceTest {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailService emailService;

    @Test
    public void testSendEmail() {
        // Arrange
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("ivanal030303@gmail.com");
        message.setSubject("Test Email");
        message.setText("This is a test email sent from Spring Boot.");

        // Act
        emailService.sendEmail("ivanal030303@gmail.com", "Test Email", "This is a test email sent from Spring Boot.");
    }
}
