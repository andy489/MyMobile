package com.mobilele.repository;

import com.mobilele.model.entity.UserRoleEntity;
import com.mobilele.model.enumerated.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

    Optional<UserRoleEntity> findByUserRoleEnum(UserRoleEnum userRoleEnum);

    List<UserRoleEntity> findAllByUserRoleEnumIn(List<UserRoleEnum> roles);
}
