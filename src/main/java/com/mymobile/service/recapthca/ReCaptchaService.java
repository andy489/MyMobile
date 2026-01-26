package com.mymobile.service.recapthca;

import com.mymobile.config.ReCaptchaConfig;
import com.mymobile.model.dto.ReCaptchaResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class ReCaptchaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReCaptchaService.class);

    private final WebClient webClient;

    private final RestTemplate restTemplate;
    private final ReCaptchaConfig reCaptchaConfig;

    public ReCaptchaService(WebClient webClient, RestTemplate restTemplate, ReCaptchaConfig reCaptchaConfig) {
        this.webClient = webClient;
        this.restTemplate = restTemplate;
        this.reCaptchaConfig = reCaptchaConfig;
    }

    public Optional<ReCaptchaResponseDto> verify(String recaptchaResponse) {
        // If token is null or empty, return empty optional
        if (recaptchaResponse == null || recaptchaResponse.isBlank()) {
            LOGGER.warn("Empty reCAPTCHA response received");
            return Optional.empty();
        }

        try {
            String url = "https://www.google.com/recaptcha/api/siteverify";

            // Build parameters - IMPORTANT: Use LinkedMultiValueMap
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("secret", reCaptchaConfig.getSecret()); // Get from configuration
            params.add("response", recaptchaResponse);

            LOGGER.debug("Verifying reCAPTCHA token: {}", recaptchaResponse.substring(0, Math.min(20, recaptchaResponse.length())) + "...");

            // Make the request
            ReCaptchaResponseDto response = restTemplate.postForObject(
                    url,
                    params,
                    ReCaptchaResponseDto.class
            );

            LOGGER.debug("reCAPTCHA verification result - Success: {}, Score: {}",
                    response != null ? response.isSuccess() : "null",
                    response != null ? response.getScore() : "null");

            return Optional.ofNullable(response);

        } catch (RestClientException e) {
            LOGGER.error("Error verifying reCAPTCHA: {}", e.getMessage(), e);
            return Optional.empty();
        }
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
