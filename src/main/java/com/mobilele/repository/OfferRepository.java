package com.mobilele.repository;

import com.mobilele.model.entity.ExchangeRateEntity;
import com.mobilele.model.entity.OfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OfferRepository extends JpaRepository<OfferEntity, String>, JpaSpecificationExecutor<OfferEntity> {


    interface ExchangeRateRepository extends JpaRepository<ExchangeRateEntity, String> {
    }
}
