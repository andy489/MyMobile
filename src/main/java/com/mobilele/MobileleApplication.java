package com.mobilele;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class MobileleApplication
//        implements CommandLineRunner
{

    private final PasswordEncoder encoder;

    @Autowired
    public MobileleApplication(PasswordEncoder encoder) {

        this.encoder = encoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(MobileleApplication.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        String[] passwords = {"1234", "1234", "1234", "1234"};
//
//        Arrays.stream(passwords).map(encoder::encode).forEach(System.out::println);
//    }

}
