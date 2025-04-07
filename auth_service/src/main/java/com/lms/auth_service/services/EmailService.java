package com.lms.auth_service.services;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email.", e);
        }
    }

    public void sendEmailWithQRCode(String email, String subject, String password, String qrCodeUrl) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(subject);

            // HTML content for the email body
            String body = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }" +
                    ".container { background-color: #ffffff; border-radius: 8px; padding: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); max-width: 600px; margin: auto; }" +
                    "h1 { color: #2e6b34; }" +
                    ".button { display: inline-block; padding: 10px 20px; font-size: 16px; color: #ffffff; background-color: #2e6b34; text-decoration: none; border-radius: 5px; }" +
                    ".button:hover { background-color: #255428; }" +
                    ".qr-code { text-align: center; margin: 20px 0; }" +
                    "p { color: #333; line-height: 1.6; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<h1>Welcome to LMS</h1>" +
                    "<p>Your account has been successfully created. Below are your credentials and instructions to set up Two-Factor Authentication:</p>" +
                    "<p><strong>Password:</strong> " + password + "</p>" +
                    "<p>To set up Google Authenticator:</p>" +
                    "<ol>" +
                    "<li>Scan the QR code below using the Google Authenticator app.</li>" +
                    "<li>Follow the instructions in the app to complete the setup.</li>" +
                    "</ol>" +
                    "<div class='qr-code'>" +
                    "<img src='cid:qrcode' alt='QR Code'>" +
                    "</div>" +
                    "<p>If you have any issues, feel free to contact our support team.</p>" +
                    "<p>Thank you,<br>The LMS Team</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(body, true);

            byte[] qrCodeImage = generateQRCodeImage(qrCodeUrl);

            helper.addInline("qrcode", new ByteArrayResource(qrCodeImage), "image/png");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send QR Code email.", e);
        } catch (IOException | WriterException e) {
            throw new RuntimeException("Failed to generate QR Code.", e);
        }
    }
    public String sendVerificationCodeEmail(String email) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Password Reset Verification Code");

            String verificationCode = generateVerificationCode();

            String body = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }" +
                    ".container { background-color: #ffffff; border-radius: 8px; padding: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); max-width: 600px; margin: auto; }" +
                    "h1 { color: #2e6b34; }" +
                    "p { color: #333; line-height: 1.6; }" +
                    ".code { font-size: 24px; color: #2e6b34; font-weight: bold; text-align: center; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<h1>Password Reset Request</h1>" +
                    "<p>You requested to reset your password. Use the verification code below to proceed with resetting your password:</p>" +
                    "<p class='code'>" + verificationCode + "</p>" +
                    "<p>If you did not request this change, please ignore this email or contact support immediately.</p>" +
                    "<p>Thank you,<br>The LMS Team</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(body, true);

            mailSender.send(message);
            return verificationCode;
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification code email.", e);
        }
    }

    public String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(999999);
        return String.format("%06d", code);
    }


    private byte[] generateQRCodeImage(String text) throws WriterException, IOException {
        BitMatrix bitMatrix = new com.google.zxing.qrcode.QRCodeWriter()
                .encode(text, BarcodeFormat.QR_CODE, 250, 250);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}
