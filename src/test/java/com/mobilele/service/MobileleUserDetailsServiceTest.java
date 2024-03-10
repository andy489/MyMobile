package com.mobilele.service;

import com.mobilele.model.user.MobileleUserDetails;
import com.mobilele.repository.UserRepository;
import com.mobilele.mapper.MapStructMapper;
import com.mobilele.model.entity.UserEntity;
import com.mobilele.model.entity.UserRoleEntity;
import com.mobilele.model.enumerated.UserRoleEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Junit 5
class MobileleUserDetailsServiceTest {

    @Mock
    private UserRepository mockUserRepo;

    @Mock
    private MapStructMapper mockMapper;

    //    Uncomment below if you do not want to use setUp method
//    @InjectMocks
    private MobileleUserDetailsService toTest;

    @BeforeEach
    void setUp() {
        toTest = new MobileleUserDetailsService(mockUserRepo, mockMapper);
    }

    @AfterEach
    void tearDown() {
        // Nothing to shut down here
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        // arrange:
        final UserRoleEntity adminRole = new UserRoleEntity().setUserRoleEnum(UserRoleEnum.ADMIN);
        final UserRoleEntity userRole = new UserRoleEntity().setUserRoleEnum(UserRoleEnum.USER);

        final SimpleGrantedAuthority adminSGA = new SimpleGrantedAuthority(UserRoleEnum.ADMIN.name());
        final SimpleGrantedAuthority userSGA = new SimpleGrantedAuthority(UserRoleEnum.USER.name());

        final UserEntity testUserEntity = new UserEntity()
                .setUsername("pesho")
                .setPassword("top-secret")
                .setEmail("test@example.com")
                .setFirstName("Pesho")
                .setLastName("Peshev")
                .setImageUrl("http://image.com/image")
                .setIsActive(true)
                .setUserRoles(List.of(adminRole, userRole));

        final MobileleUserDetails testMobileleUserDetails = new MobileleUserDetails()
                .setUsername(testUserEntity.getUsername())
                .setPassword(testUserEntity.getPassword())
                .setEmail(testUserEntity.getEmail())
                .setFirstName(testUserEntity.getFirstName())
                .setLastName(testUserEntity.getLastName())
                .setAuthorities(List.of(adminSGA, userSGA));

        when(mockUserRepo.findByUsername(testUserEntity.getUsername()))
                .thenReturn(Optional.of(testUserEntity));

        when(mockMapper.toUserDetails(testUserEntity)).thenReturn(testMobileleUserDetails);

        for (UserRoleEntity userRoleEntity : testUserEntity.getUserRoles()) {
            when(mockMapper.toGrantedAuthority(userRoleEntity)).thenReturn(
                    new SimpleGrantedAuthority(userRoleEntity.getUserRoleEnum().name())
            );
        }
        // EO: arrange

        // act:
        MobileleUserDetails userDetails = (MobileleUserDetails) toTest.loadUserByUsername(testUserEntity.getUsername());
        // EO: act

        // assert:
        List<String> list1 = testUserEntity.getUserRoles().stream()
                .map(ur -> ur.getUserRoleEnum().name())
                .toList();

        List<String> list2 = testMobileleUserDetails.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();

        assertAll(
                () -> assertNotNull(userDetails, "Expected not null userDetails object"),

                () -> assertEquals(userDetails.getUsername(), testUserEntity.getUsername(),
                        "Expect usernames to match"),
                () -> assertEquals(userDetails.getPassword(), testUserEntity.getPassword(),
                        "Expect passwords to match"),
                () -> assertEquals(userDetails.getFirstName(), testUserEntity.getFirstName(),
                        "Expect first name to match"),
                () -> assertEquals(userDetails.getLastName(), testUserEntity.getLastName(),
                        "Expect last name to match"),
                () -> assertEquals(userDetails.getFullName(), testUserEntity.getFullName(),
                        "Expect full name to match"),

                () -> assertTrue(list1.containsAll(list2) &&
                                list2.containsAll(list1) &&
                                list1.size() == list2.size(),
                        "Expect roles to match")
        );
        // EO: assert
    }

    @Test
    void testLoadUserByUsername_UserDoesNotExist() {
        // arrange
        // NOTE: No need to arrange anything, mocks return empty optionals by default.

        // act (in lambda) and assert
        assertThrows(UsernameNotFoundException.class, () -> toTest.loadUserByUsername("NON_EXISTENT_USERNAME"),
                "Expected UsernameNotFoundException to be thrown");
    }


}
