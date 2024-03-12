package com.mymobile.repository;

import com.mymobile.model.entity.UserRoleEntity;
import com.mymobile.model.enumerated.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

    Optional<UserRoleEntity> findByUserRoleEnum(UserRoleEnum userRoleEnum);

    List<UserRoleEntity> findAllByUserRoleEnumIn(List<UserRoleEnum> roles);
}
