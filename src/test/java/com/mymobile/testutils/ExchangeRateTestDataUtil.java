package com.mymobile.testutils;

import com.mymobile.model.entity.ExchangeRateEntity;
import com.mymobile.model.entity.UserEntity;
import com.mymobile.repository.ExchangeRateRepository;
import com.mymobile.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ExchangeRateTestDataUtil {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    public void createExchangeRate(String currency, BigDecimal rate) {

        ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity()
                .setCurrency(currency)
                .setRate(rate);

        exchangeRateRepository.save(exchangeRateEntity);
    }

    public String createTestOffer(UserEntity owner) {

        return null;
    }

    public void cleanAllTestData() {
        exchangeRateRepository.deleteAll();
    }

}
