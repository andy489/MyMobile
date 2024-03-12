package com.mymobile.testutils;

import com.mymobile.model.entity.UserRoleEntity;
import com.mymobile.model.enumerated.UserRoleEnum;
import com.mymobile.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DBInit implements CommandLineRunner {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRoleRepository.count() == 0) {
            userRoleRepository.saveAll(List.of(
                            new UserRoleEntity().setUserRoleEnum(UserRoleEnum.USER),
                            new UserRoleEntity().setUserRoleEnum(UserRoleEnum.MODERATOR),
                            new UserRoleEntity().setUserRoleEnum(UserRoleEnum.ADMIN)
                    )
            );
        }
    }
}
