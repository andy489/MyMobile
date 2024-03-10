package com.mobilele.testutils;

import com.mobilele.model.entity.ExchangeRateEntity;
import com.mobilele.model.entity.UserEntity;
import com.mobilele.repository.ExchangeRateRepository;
import com.mobilele.repository.OfferRepository;
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
