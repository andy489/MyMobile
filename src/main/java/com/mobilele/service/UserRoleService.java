package com.mobilele.service;

import com.mobilele.repository.UserRoleRepository;
import com.mobilele.model.entity.UserRoleEntity;
import com.mobilele.model.enumerated.UserRoleEnum;
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
