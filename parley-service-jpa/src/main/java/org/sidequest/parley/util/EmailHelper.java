package org.sidequest.parley.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class EmailHelper {

    @Value("${spring.mail.host}")
    private String smtpHost;

    @Value("${spring.mail.port}")
    private int smtpPort;

    @Value("${spring.mail.username}")
    private String smtpUsername;

    @Value("${spring.mail.password}")
    private String smtpPassword;

    @Value("${spring.mail.from}")
    private String fromEmail;

    public void sendPasswordResetEmail(String email, String resetToken) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", String.valueOf(smtpPort));

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Password Reset Request");

            String resetLink = "http://localhost:8080/parley/reset-password?token=" + resetToken;
            StringBuilder emailContent = new StringBuilder();
            emailContent.append("Hello,\n\n");
            emailContent.append("A password reset was requested for your Parley account.\n\n");
            emailContent.append("Click the following link to reset your password:\n");
            emailContent.append(resetLink).append("\n\n");
            emailContent.append("This link will expire in 24 hours.\n\n");
            emailContent.append("If you did not request this password reset, please ignore this email.\n\n");
            emailContent.append("Best regards,\nThe Parley Team");

            message.setText(emailContent.toString());

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }
}