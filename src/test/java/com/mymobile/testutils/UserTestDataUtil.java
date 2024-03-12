package com.mymobile.testutils;

import com.mymobile.repository.UserRepository;
import com.mymobile.repository.UserRoleRepository;
import com.mymobile.model.entity.UserEntity;
import com.mymobile.model.entity.UserRoleEntity;
import com.mymobile.model.enumerated.UserRoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserTestDataUtil {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public UserEntity createTestUser(String username) {

        return createUser(username, List.of(UserRoleEnum.USER));
    }

    public UserEntity createTestAdmin(String username) {

        return createUser(username, List.of(UserRoleEnum.ADMIN));
    }

    private UserEntity createUser(String username, List<UserRoleEnum> roles) {
        List<UserRoleEntity> roleEntities = userRoleRepository.findAllByUserRoleEnumIn(roles);

        UserEntity newUser = new UserEntity()
                .setUsername(username)
                .setPassword("top-secret")
                .setIsActive(true)
                .setEmail(username + "@example.com")
                .setFirstName("TestFirstName")
                .setLastName("TestLastName")
                .setUserRoles(roleEntities);

        return userRepository.save(newUser);
    }

    public void cleanUp() {
        userRepository.deleteAll();
    }
}
