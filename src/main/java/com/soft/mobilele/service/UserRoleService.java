package com.soft.mobilele.service;

import com.soft.mobilele.model.entity.UserRoleEntity;
import com.soft.mobilele.model.enumarated.UserRoleEnum;
import com.soft.mobilele.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public UserRoleEntity getByUserRoleEnum(UserRoleEnum userRoleEnum) {
        return userRoleRepository.findByUserRoleEnum(userRoleEnum)
                .orElseThrow(() -> new NoSuchElementException("Invalid user role (getByUserRoleEnum)"));
    }
}
