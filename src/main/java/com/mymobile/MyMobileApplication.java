package com.mymobile;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@OpenAPIDefinition(
        info = @Info(
                title = "MyMobile",
                version = "0.0.1",
                description = "The REST API of MyMobile"
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local server"
                )
        }
)
@EnableScheduling
@SpringBootApplication
public class MyMobileApplication
//        implements CommandLineRunner
{

    private final PasswordEncoder encoder;

    @Autowired
    public MyMobileApplication(PasswordEncoder encoder) {

        this.encoder = encoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(MyMobileApplication.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        String[] passwords = {"1234", "1234", "1234", "1234"};
//
//        Arrays.stream(passwords).map(encoder::encode).forEach(System.out::println);
//    }

}
