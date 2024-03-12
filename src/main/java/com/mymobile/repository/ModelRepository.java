package com.mymobile.repository;

import com.mymobile.model.entity.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<ModelEntity, Long> {
}
