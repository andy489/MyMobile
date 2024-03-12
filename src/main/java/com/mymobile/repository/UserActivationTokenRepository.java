package com.mymobile.repository;

import com.mymobile.model.entity.UserActivationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivationTokenRepository extends JpaRepository<UserActivationTokenEntity, Long> {
}
