package com.mymobile.repository;

import com.mymobile.model.entity.ExchangeRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRateEntity, String> {

}
