package com.mymobile.repository;

import com.mymobile.model.entity.ExchangeRateEntity;
import com.mymobile.model.entity.OfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OfferRepository extends JpaRepository<OfferEntity, String>, JpaSpecificationExecutor<OfferEntity> {


    interface ExchangeRateRepository extends JpaRepository<ExchangeRateEntity, String> {
    }
}
