package com.mobilele.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "google.recaptcha")
@Getter
@Setter
@Accessors(chain = true)
public class ReCaptchaConfig {

    private String site;

    private String secret;
}
