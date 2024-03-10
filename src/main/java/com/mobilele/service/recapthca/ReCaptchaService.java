package com.mobilele.service.recapthca;

import com.mobilele.config.ReCaptchaConfig;
import com.mobilele.model.dto.ReCaptchaResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class ReCaptchaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReCaptchaService.class);

    private final WebClient webClient;

    private final ReCaptchaConfig reCaptchaConfig;

    public ReCaptchaService(WebClient webClient, ReCaptchaConfig reCaptchaConfig) {
        this.webClient = webClient;
        this.reCaptchaConfig = reCaptchaConfig;
    }

    public Optional<ReCaptchaResponseDto> verify(String token) {
        return Optional.ofNullable(webClient.post()
                .uri(this::buildReCaptchaURI)
                .body(BodyInserters
                        .fromFormData("secret", reCaptchaConfig.getSecret())
                        .with("response", token))
                .retrieve()
                .bodyToMono(ReCaptchaResponseDto.class)
                .doOnError(t -> LOGGER.error("Error fetching google response...", t))
                .onErrorComplete()
                .block());
    }

    private URI buildReCaptchaURI(UriBuilder uriBuilder) {
        // REST endpoint for google verification
        // https://www.google.com/recaptcha/api/siteverify

        return uriBuilder
                .scheme("https")
                .host("www.google.com")
                .path("/recaptcha/api/siteverify")
                .build();
    }


}
