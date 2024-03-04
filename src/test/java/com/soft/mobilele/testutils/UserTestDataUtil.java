package com.soft.mobilele.testutils;

import com.soft.mobilele.model.entity.UserEntity;
import com.soft.mobilele.model.entity.UserRoleEntity;
import com.soft.mobilele.model.enumerated.UserRoleEnum;
import com.soft.mobilele.repository.UserRepository;
import com.soft.mobilele.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserTestDataUtil {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public UserEntity createTestUser() {
        return createUser(List.of(UserRoleEnum.USER));
    }

    public UserEntity createTestAdmin() {
        return createUser(List.of(UserRoleEnum.ADMIN));
    }

    private UserEntity createUser(List<UserRoleEnum> roles) {
        List<UserRoleEntity> roleEntities = userRoleRepository.findAllByUserRoleEnumIn(roles);

        UserEntity newUser = new UserEntity()
                .setIsActive(true)
                .setEmail("test@example.com")
                .setFirstName("TestFirstName")
                .setLastName("TestLastName")
                .setUserRoles(roleEntities);

        return userRepository.save(newUser);
    }

    public void cleanUp() {
        userRepository.deleteAll();
    }
}
