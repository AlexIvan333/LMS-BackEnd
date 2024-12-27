package com.lms.backend.services;
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

@Service
public class EmailService{
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

    public void sendEmailWithQRCode(String email, String subject, String body, String qrCodeUrl) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, true);

            // Catch WriterException and IOException
            byte[] qrCodeImage;
            try {
                qrCodeImage = generateQRCodeImage(qrCodeUrl);
            } catch (WriterException | IOException e) {
                throw new RuntimeException("Failed to generate QR code image.", e);
            }

            helper.addAttachment("qrcode.png", new ByteArrayResource(qrCodeImage));
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send QR Code email.", e);
        }
    }


    private byte[] generateQRCodeImage(String text) throws WriterException, IOException {
        BitMatrix bitMatrix = new com.google.zxing.qrcode.QRCodeWriter()
                .encode(text, BarcodeFormat.QR_CODE, 250, 250);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}
