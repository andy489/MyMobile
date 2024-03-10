package com.mobilele.service;

import com.mobilele.service.aop.WarnIfExecutionExceeds;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

@Service
public class MailService {

    private final TemplateEngine templateEngine;

    private final MessageSource messageSource;

    private final JavaMailSender javaMailSender;

    private final String appMail;

    public MailService(TemplateEngine templateEngine,
                       MessageSource messageSource,
                       JavaMailSender javaMailSender,
                       @Value("${mail.app-mail}") String appMail) {

        this.templateEngine = templateEngine;
        this.messageSource = messageSource;
        this.javaMailSender = javaMailSender;
        this.appMail = appMail;
    }

    @WarnIfExecutionExceeds(
            timeInMillis = 800L
    )
    public void sendRegistrationEmail(String userEmail, String username, Locale locale, String activationToken) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setFrom(appMail);
            mimeMessageHelper.setReplyTo(appMail);
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setSubject(getEmailSubject(locale));
            mimeMessageHelper.setText(generateMessageContent(locale, username, activationToken), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getEmailSubject(Locale locale) {
        return messageSource.getMessage("registration_email_subject", new Object[0], locale);
    }

    private String generateMessageContent(Locale locale, String username, String activationToken) {
        Context context = new Context();

        // TODO: url to append activation code: localhost...
        context.setVariable("username", username);
        context.setVariable("activation_token", activationToken);
        context.setLocale(locale);

        return templateEngine.process("email/registration-email", context);
    }

}
