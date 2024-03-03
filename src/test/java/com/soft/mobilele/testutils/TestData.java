package com.soft.mobilele.testutils;

import com.soft.mobilele.model.entity.ExchangeRateEntity;
import com.soft.mobilele.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TestData {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    public void createExchangeRate(String currency, BigDecimal rate) {

        ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity()
                .setCurrency(currency)
                .setRate(rate);

        exchangeRateRepository.save(exchangeRateEntity);
    }

    public void cleanAllTestData(){
        exchangeRateRepository.deleteAll();
    }

}
