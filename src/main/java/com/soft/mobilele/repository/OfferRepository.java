package com.soft.mobilele.repository;

import com.soft.mobilele.model.entity.OfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OfferRepository extends JpaRepository<OfferEntity, String>, JpaSpecificationExecutor<OfferEntity> {



}
