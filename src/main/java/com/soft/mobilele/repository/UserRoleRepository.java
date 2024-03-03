package com.soft.mobilele.repository;

import com.soft.mobilele.model.entity.UserRoleEntity;
import com.soft.mobilele.model.enumerated.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

    Optional<UserRoleEntity> findByUserRoleEnum(UserRoleEnum userRoleEnum);
}
