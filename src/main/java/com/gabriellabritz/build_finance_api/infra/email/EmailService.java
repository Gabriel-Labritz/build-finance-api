package com.gabriellabritz.build_finance_api.infra.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${app.email.origin}")
    private String originEmail;

    @Value("${app.email.sender}")
    private String sender;

    @Value("${app.url.site}")
    private String urlSite;

    @Value("classpath:templates/email-verification-template.html")
    private Resource emailVerificationTemplate;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(String email, String subject, String bodyEmail) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(originEmail, sender);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(bodyEmail, true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(message);
    }

    public void sendEmailVerification(String userName, String userEmail, String token) {
        String verificationUrl = urlSite.concat("/account/verify-account?token=").concat(token);
        String subject = "Build Finance - Verifique sua conta";

        String bodyEmail = loadTemplate(emailVerificationTemplate)
                .replace("{userName}", userName)
                .replace("{verificationUrl}", verificationUrl);

        sendEmail(userEmail, subject, bodyEmail);
    }

    private String loadTemplate(Resource resource) {
        try {
            return resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar template", e);
        }
    }
}
