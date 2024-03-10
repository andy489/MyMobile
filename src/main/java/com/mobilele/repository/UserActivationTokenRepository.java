package com.mobilele.repository;

import com.mobilele.model.entity.UserActivationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivationTokenRepository extends JpaRepository<UserActivationTokenEntity, Long> {
}
