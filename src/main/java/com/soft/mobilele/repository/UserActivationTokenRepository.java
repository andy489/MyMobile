package com.soft.mobilele.repository;

import com.soft.mobilele.model.entity.UserActivationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivationTokenRepository extends JpaRepository<UserActivationTokenEntity, Long> {
}
