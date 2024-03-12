package com.mymobile.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class CurrencyRatesErrorHandler implements ResponseErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyRatesErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode() != HttpStatus.OK;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        LOGGER.warn("Filed to refresh exchange rates. {}.", response.getStatusText());
    }
}
