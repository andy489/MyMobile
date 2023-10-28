package com.soft.mobilele.repository;

import com.soft.mobilele.model.entity.UserActivationLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivationLinkRepository extends JpaRepository<UserActivationLinkEntity, Long> {
}
