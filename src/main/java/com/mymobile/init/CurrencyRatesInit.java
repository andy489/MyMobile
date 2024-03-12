package com.mymobile.init;

import com.mymobile.config.OpenExchangeRateConfig;
import com.mymobile.model.dto.ExchangeRatesDto;
import com.mymobile.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Component
public class CurrencyRatesInit implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyService.class);


    // https://docs.openexchangerates.org/reference/latest-json
    // https://openexchangerates.org/api/latest.json?app_id=434ee4cf9aba48afa975132cc3fd2d8d&symbols=BGN,EUR,GBP

    private final OpenExchangeRateConfig openExchangeRateConfig;

    private final RestTemplate restTemplate;

    private final CurrencyService currencyService;

    public CurrencyRatesInit(OpenExchangeRateConfig openExchangeRateConfig,
                             RestTemplate restTemplate,
                             CurrencyService currencyService) {

        this.openExchangeRateConfig = openExchangeRateConfig;
        this.restTemplate = restTemplate;
        this.currencyService = currencyService;

        restTemplate.setErrorHandler(new CurrencyRatesErrorHandler());
    }

    @Override
    public void run(String... args) throws Exception {
        if (openExchangeRateConfig.getEnabled()) {
//        System.out.println(openExchangeRateConfig);

            String openExchangeRateUrlTemplate = new StringBuilder()
                    .append(openExchangeRateConfig.getSchema())
                    .append("://")
                    .append(openExchangeRateConfig.getHost())
                    .append(openExchangeRateConfig.getPath())
                    .append("?app_id={app_id}&symbols={symbols}")
                    .toString();

            Map<String, String> requestParams = Map.of(
                    "app_id", openExchangeRateConfig.getAppId(),
                    "symbols", String.join(",", openExchangeRateConfig.getSymbols())
            );

            try {
                ExchangeRatesDto exchangeRatesDto = restTemplate
                        .getForObject(openExchangeRateUrlTemplate,
                                ExchangeRatesDto.class,
                                requestParams);

                assert exchangeRatesDto != null;
                if (!Objects.isNull(exchangeRatesDto.getBase())) {

                    LOGGER.info("Received from OPEN EXCHANGE RATES: {}.", exchangeRatesDto);

                    currencyService.refreshRates(exchangeRatesDto);
                }
            } catch (RuntimeException ignored) {
            }
        }
    }
}
