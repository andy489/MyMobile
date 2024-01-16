package com.soft.mobilele.init;

import com.soft.mobilele.config.OpenExchangeRateConfig;
import com.soft.mobilele.model.dto.ExchangeRatesDto;
import com.soft.mobilele.service.CurrencyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CurrencyRatesInit implements CommandLineRunner {

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

            ExchangeRatesDto exchangeRatesDto = restTemplate
                    .getForObject(openExchangeRateUrlTemplate,
                            ExchangeRatesDto.class,
                            requestParams);

            System.out.println("-------- RECEIVED FROM OPEN EXCHANGE RATES --------");
            System.out.println(exchangeRatesDto);
            System.out.println("-------- RECEIVED FROM OPEN EXCHANGE RATES --------");

            currencyService.refreshRates(exchangeRatesDto);
        }
    }
}
