package com.soft.mobilele.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {

    @Bean
    public JavaMailSender javaMailSender(@Value("${mail.host}") String host,
                                         @Value("${mail.port}") Integer port,
                                         @Value("${mail.username}") String username,
                                         @Value("${mail.password}") String password) {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(host);
        javaMailSender.setPort(port);

        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);

        javaMailSender.setJavaMailProperties(mailProperties());
        javaMailSender.setDefaultEncoding("UTF-8");

        return javaMailSender;
    }

    private Properties mailProperties() {

        Properties properties = new Properties();

        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.transport.protocol", "smtp");

        return properties;
    }
}
