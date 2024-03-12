package com.mymobile.service;

import com.mymobile.model.dto.ExchangeRatesDto;
import com.mymobile.model.dto.rest.ConvertRequestDto;
import com.mymobile.model.dto.rest.MoneyDto;
import com.mymobile.model.entity.ExchangeRateEntity;
import com.mymobile.config.OpenExchangeRateConfig;
import com.mymobile.model.exception.ResourceNotFoundException;
import com.mymobile.repository.ExchangeRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;

@Service
public class CurrencyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyService.class);

    private final ExchangeRateRepository exchangeRateRepository;

    private final OpenExchangeRateConfig openExchangeRateConfig;

    @Autowired
    public CurrencyService(ExchangeRateRepository exchangeRateRepository,
                           OpenExchangeRateConfig openExchangeRateConfig) {

        this.exchangeRateRepository = exchangeRateRepository;
        this.openExchangeRateConfig = openExchangeRateConfig;
    }

    public void refreshRates(ExchangeRatesDto exchangeRatesDto) {

        LOGGER.info("Exchange rates received {}", exchangeRatesDto);
        exchangeRateRepository.deleteAll();

        Integer decimalPrecision = openExchangeRateConfig.getDecimalPrecision();

        BigDecimal BGN_TO_USD = getExchangeRate(exchangeRatesDto, "BGN", "USD", decimalPrecision)
                .orElse(null);
        BigDecimal BGN_TO_EUR = getExchangeRate(exchangeRatesDto, "BGN", "EUR", decimalPrecision)
                .orElse(null);
        BigDecimal BGN_TO_GBP = getExchangeRate(exchangeRatesDto, "BGN", "GBP", decimalPrecision)
                .orElse(null);

        if (BGN_TO_USD != null) {
            ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity().setCurrency("USD").setRate(BGN_TO_USD);
            exchangeRateRepository.save(exchangeRateEntity);
        } else {
            LOGGER.error("Unable to get exchange rate for BGN to USD");
        }

        if (BGN_TO_EUR != null) {
            ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity().setCurrency("EUR").setRate(BGN_TO_EUR);
            exchangeRateRepository.save(exchangeRateEntity);
        } else {
            LOGGER.error("Unable to get exchange rate for BGN to EUR");
        }

        if (BGN_TO_GBP != null) {
            ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity().setCurrency("GBP").setRate(BGN_TO_GBP);
            exchangeRateRepository.save(exchangeRateEntity);
        } else {
            LOGGER.error("Unable to get exchange rate for BGN to GBP");
        }

        LOGGER.info("Rates refreshed...");
    }

    public MoneyDto convert(ConvertRequestDto convertRequestDto) {

        ExchangeRateEntity exchangeRateEntity = exchangeRateRepository.findById(convertRequestDto.target())
                .orElseThrow(() -> new ResourceNotFoundException("Conversion to target " +
                        convertRequestDto.target() + " not possible!"));

        return new MoneyDto(
                convertRequestDto.target(),
                exchangeRateEntity.getRate().multiply(convertRequestDto.amount())
        );
    }

    private static Optional<BigDecimal> getExchangeRate(ExchangeRatesDto exchangeRatesDto, String from, String to,
                                                        Integer decimalPrecision) {
        Objects.requireNonNull(from, "From target cannot be null");
        Objects.requireNonNull(to, "From target cannot be null");

//        {
//            "base": "USD",
//            "rates": {
//                "BGN": 1.783335,
//                "EUR": 0.91328,
//                "GBP": 0.784956
//            }
//        }

        // e.g. USD -> USD
        if (Objects.equals(from, to)) {
            return Optional.of(BigDecimal.ONE);
        }

        // e.g. USD -> BGN
        if (from.equals(exchangeRatesDto.getBase())) {
            if (exchangeRatesDto.getRates().containsKey(to)) {
                return Optional.of(exchangeRatesDto.getRates().get(to));
            }
        } else if (Objects.equals(to, exchangeRatesDto.getBase())) {
            // e.g. BGN -> USD
            if (exchangeRatesDto.getRates().containsKey(from)) {
                return Optional.of(BigDecimal.ONE.divide(exchangeRatesDto.getRates().get(from),
                        3, RoundingMode.DOWN));
            }
        } else if (exchangeRatesDto.getRates().containsKey(to) && exchangeRatesDto.getRates().containsKey(from)) {
            return Optional.of(exchangeRatesDto.getRates().get(to).divide(exchangeRatesDto.getRates().get(from),
                    3, RoundingMode.DOWN));
        }


        return Optional.empty();
    }
}
