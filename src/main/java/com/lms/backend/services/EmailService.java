package com.lms.backend.services;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class EmailService{
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (MailAuthenticationException ex) {
            throw new RuntimeException("Failed to authenticate with the email server: " + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Failed to send email: " + ex.getMessage());
        }
    }

    public void sendEmailWithQRCode(String email, String subject, String body, String qrCodeUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, true);

            byte[] qrCodeImage = generateQRCodeImage(qrCodeUrl);
            helper.addAttachment("2fa-setup.png", new ByteArrayResource(qrCodeImage));

            mailSender.send(message);
        } catch (MessagingException | IOException | WriterException e) {
            throw new RuntimeException("Failed to send email with QR code.", e);
        }
    }

//    public void sendTwoFactorCode(String email, int code) {
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//            helper.setTo(email);
//            helper.setSubject("Your 2FA Code");
//            helper.setText("Your 2FA code is: " + code, true);
//
//            mailSender.send(message);
//        } catch (MessagingException e) {
//            throw new RuntimeException("Failed to send email.");
//        }
//    }

    private byte[] generateQRCodeImage(String text) throws WriterException, IOException {
        BitMatrix bitMatrix = new com.google.zxing.qrcode.QRCodeWriter()
                .encode(text, BarcodeFormat.QR_CODE, 250, 250);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}
