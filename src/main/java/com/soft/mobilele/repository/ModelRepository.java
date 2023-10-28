package com.soft.mobilele.repository;

import com.soft.mobilele.model.entity.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<ModelEntity, Long> {
}
