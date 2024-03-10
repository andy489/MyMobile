package com.mobilele.testutils;

import com.mobilele.model.entity.BrandEntity;
import com.mobilele.model.entity.ExchangeRateEntity;
import com.mobilele.model.entity.ModelEntity;
import com.mobilele.model.entity.OfferEntity;
import com.mobilele.model.entity.UserEntity;
import com.mobilele.model.enumerated.CategoryEnum;
import com.mobilele.model.enumerated.EngineEnum;
import com.mobilele.model.enumerated.TransmissionEnum;
import com.mobilele.repository.BrandRepository;
import com.mobilele.repository.ExchangeRateRepository;
import com.mobilele.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class OfferTestDataUtil {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private BrandRepository brandRepository;


    public void createExchangeRate(String currency, BigDecimal rate) {
        exchangeRateRepository.save(
                new ExchangeRateEntity().setCurrency(currency).setRate(rate)
        );
    }

    public OfferEntity createTestOffer(UserEntity seller) {

        // create test brand
        BrandEntity testBrand = brandRepository.save(new BrandEntity()
                .setName("test Brand")
                .setModels(List.of(
                                new ModelEntity()
                                        .setStartYear(1990)
                                        .setCategory(CategoryEnum.CAR)
                                        .setName("Test Model 1")
                                        .setImageUrl("https://www.random-image.com/test-model1.jpg"),
                                new ModelEntity()
                                        .setStartYear(2001)
                                        .setCategory(CategoryEnum.CAR)
                                        .setName("Test Model 2")
                                        .setImageUrl("https://www.random-image.com/test-model2.jpg")
                        )
                )
        );

        // create test offer
        OfferEntity offer = new OfferEntity()
                .setId(UUID.randomUUID().toString())
                .setModel(testBrand.getModels().get(0))
                .setImageUrl("https://www.random-image.com/test.jpg")
                .setPrice(BigDecimal.valueOf(1000))
                .setYear(2020)
                .setDescription("Test Description")
                .setEngine(EngineEnum.GASOLINE)
                .setTransmission(TransmissionEnum.AT)
                .setMileage(10000)
                .setSeller(seller);

        return offerRepository.save(offer);
    }

    public void cleanUp() {
        exchangeRateRepository.deleteAll();
        offerRepository.deleteAll();
        brandRepository.deleteAll();
    }
}
