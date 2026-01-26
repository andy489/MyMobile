package com.mymobile.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@Accessors(chain = true)
public class ReCaptchaConfig {

    private String site;
    private String secret;

    public ReCaptchaConfig() {
        Dotenv dotenv = Dotenv.configure()
                .directory("config")
                .filename(".env")
                .ignoreIfMissing()
                .load();

        this.site = dotenv.get("RECAPTCHA_SITE_KEY");
        this.secret = dotenv.get("RECAPTCHA_SITE_SECRET");
    }

    @PostConstruct
    public void verify() {
        System.out.println("=== POSTCONSTRUCT VERIFICATION ===");
        System.out.println("Site key: " + (this.site != null ? this.site.substring(0, 10) + "..." : "null"));
        System.out.println("Secret: " + (this.secret != null ? "[SET]" : "null"));
    }
}