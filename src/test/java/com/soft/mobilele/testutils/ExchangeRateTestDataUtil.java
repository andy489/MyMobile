package com.soft.mobilele.testutils;

import com.soft.mobilele.model.entity.ExchangeRateEntity;
import com.soft.mobilele.model.entity.UserEntity;
import com.soft.mobilele.model.entity.UserRoleEntity;
import com.soft.mobilele.model.enumerated.UserRoleEnum;
import com.soft.mobilele.repository.ExchangeRateRepository;
import com.soft.mobilele.repository.OfferRepository;
import com.soft.mobilele.repository.UserRepository;
import com.soft.mobilele.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

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
