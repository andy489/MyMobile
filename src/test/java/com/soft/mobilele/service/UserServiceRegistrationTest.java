package com.soft.mobilele.service;

import com.soft.mobilele.mapper.MapStructMapper;
import com.soft.mobilele.model.dto.UserRegistrationDTO;
import com.soft.mobilele.model.entity.UserEntity;
import com.soft.mobilele.model.entity.UserRoleEntity;
import com.soft.mobilele.model.enumarated.UserRoleEnum;
import com.soft.mobilele.model.user.MobileleUserDetails;
import com.soft.mobilele.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceRegistrationTest {

    private final static String testUsername = "testUsername";
    private final static String testEmail = "test@test.com";
    private final static String testFirstName = "Test";
    private final static String testLastName = "Testov";
    private final static String testPassword = "top-secret";
    private final static String testConfirmPassword = "top-secret";
    private final static String testImageUrl = "http://image.url";
    private final static Long testUserEntityId = 1L;
    private final static Long getTestUserEntityId = 3L;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private MapStructMapper mockMapper;

    @Mock
    private UserDetailsService mockUserDetailsService;

    @Mock
    private UserRoleService mockUserRoleService;

    @Mock
    private EmailService mockEmailService;

    @Mock
    private Locale mockLocaleResolver;

    @Captor
    private ArgumentCaptor<UserEntity> userEntityArgumentCaptor;

    @Mock
    private UserService toTest;


    @BeforeEach
    void setUp() {
        toTest = new UserService(
                mockUserRepository,
                mockPasswordEncoder,
                mockMapper,
                mockUserDetailsService,
                mockUserRoleService,
                mockEmailService
        );

        // lenient example
        UserEntity userEntity = new UserEntity().setUsername("UNNECESARY_STUBBING_EXAMPLE");
        lenient().when(mockUserRepository.save(userEntity)).thenReturn(userEntity);
    }

    @AfterEach
    void tearDown() {
        // Nothing to shut down here
    }


    @Test
    void testUserRegistration_SaveInvoked_v1() {
        // arrange
        UserRegistrationDTO testUserRegistrationDTO = new UserRegistrationDTO()
                .setUsername(testUsername)
                .setEmail(testEmail)
                .setFirstName(testFirstName)
                .setLastName(testLastName)
                .setPassword(testPassword)
                .setConfirmPassword(testConfirmPassword);

        UserEntity testUserEntity = new UserEntity()
                .setUsername(testUserRegistrationDTO.getUsername())
                .setEmail(testUserRegistrationDTO.getEmail())
                .setFirstName(testUserRegistrationDTO.getFirstName())
                .setLastName(testUserRegistrationDTO.getLastName())
                .setImageUrl(testImageUrl);

        UserRoleEntity testUserRoleEntity = new UserRoleEntity()
                .setId(getTestUserEntityId)
                .setUserRoleEnum(UserRoleEnum.USER);

        UserEntity testReturnedUserEntity = new UserEntity()
                .setUsername(testUserEntity.getUsername())
                .setEmail(testUserEntity.getEmail())
                .setFirstName(testUserEntity.getFirstName())
                .setLastName(testUserEntity.getLastName())
                .setIsActive(true)
                .setImageUrl(testUserEntity.getImageUrl())
                .setUserRoles(List.of(testUserRoleEntity))
                .setId(testUserEntityId);

        final String ENCODED_PASS = "ENCODED" + testUserRegistrationDTO.getPassword() + "ENCODED";

        MobileleUserDetails testUserDetails = new MobileleUserDetails()
                .setUsername(testReturnedUserEntity.getUsername())
                .setEmail(testReturnedUserEntity.getEmail())
                .setFirstName(testReturnedUserEntity.getFirstName())
                .setLastName(testReturnedUserEntity.getLastName())
                .setPassword(testReturnedUserEntity.getPassword())
                .setAuthorities(
                        testReturnedUserEntity.getUserRoles().stream()
                                .map(r -> new SimpleGrantedAuthority(r.getUserRoleEnum().name()))
                                .toList()
                );

        Consumer<Authentication> dummyAuthenticationConsumerStub = (a) -> {
        };

        when(mockMapper.toEntity(testUserRegistrationDTO))
                .thenReturn(testUserEntity);

        when(mockPasswordEncoder.encode(testUserRegistrationDTO.getPassword()))
                .thenReturn(ENCODED_PASS);

        when(mockUserRoleService.getByUserRoleEnum(UserRoleEnum.USER))
                .thenReturn(testUserRoleEntity);

        when(mockUserRepository.save(testUserEntity)).
                thenReturn(testReturnedUserEntity);

        when(mockUserDetailsService.loadUserByUsername(testReturnedUserEntity.getUsername()))
                .thenReturn(testUserDetails);

        // EO: arrange

        // act
        toTest.registerAndLogin(
                testUserRegistrationDTO,
                mockLocaleResolver,
                dummyAuthenticationConsumerStub
        );
        // EO: act

        // assert
        verify(mockUserRepository).save(any());
        // EO: assert
    }

    @Test
    void testUserRegistration_SaveInvoked_v2_ArgumentCaptor() {
        // arrange
        UserRegistrationDTO testUserRegistrationDTO = new UserRegistrationDTO()
                .setUsername(testUsername)
                .setEmail(testEmail)
                .setFirstName(testFirstName)
                .setLastName(testLastName)
                .setPassword(testPassword)
                .setConfirmPassword(testConfirmPassword);

        UserEntity testUserEntity = new UserEntity()
                .setUsername(testUserRegistrationDTO.getUsername())
                .setEmail(testUserRegistrationDTO.getEmail())
                .setFirstName(testUserRegistrationDTO.getFirstName())
                .setLastName(testUserRegistrationDTO.getLastName())
                .setImageUrl(testImageUrl);

        UserRoleEntity testUserRoleEntity = new UserRoleEntity()
                .setId(3L)
                .setUserRoleEnum(UserRoleEnum.USER);

        final String ENCODED_PASS = "ENCODED" + testUserRegistrationDTO.getPassword() + "ENCODED";

        UserEntity testReturnedUserEntity = new UserEntity()
                .setUsername(testUserEntity.getUsername())
                .setEmail(testUserEntity.getEmail())
                .setFirstName(testUserEntity.getFirstName())
                .setLastName(testUserEntity.getLastName())
                .setIsActive(true)
                .setImageUrl(testUserEntity.getImageUrl())
                .setPassword(ENCODED_PASS)
                .setUserRoles(List.of(testUserRoleEntity))
                .setId(1L);

        MobileleUserDetails testUserDetails = new MobileleUserDetails()
                .setUsername(testReturnedUserEntity.getUsername())
                .setEmail(testReturnedUserEntity.getEmail())
                .setFirstName(testReturnedUserEntity.getFirstName())
                .setLastName(testReturnedUserEntity.getLastName())
                .setPassword(testReturnedUserEntity.getPassword())
                .setAuthorities(
                        testReturnedUserEntity.getUserRoles().stream()
                                .map(r -> new SimpleGrantedAuthority(r.getUserRoleEnum().name()))
                                .toList()
                );

        Consumer<Authentication> dummyAuthenticationConsumerStub = (a) -> {
        };

        when(mockMapper.toEntity(testUserRegistrationDTO))
                .thenReturn(testUserEntity);

        when(mockPasswordEncoder.encode(testUserRegistrationDTO.getPassword()))
                .thenReturn(ENCODED_PASS);

        when(mockUserRoleService.getByUserRoleEnum(UserRoleEnum.USER))
                .thenReturn(testUserRoleEntity);

        when(mockUserRepository.save(testUserEntity)).
                thenReturn(testReturnedUserEntity);

        when(mockUserDetailsService.loadUserByUsername(testReturnedUserEntity.getUsername()))
                .thenReturn(testUserDetails);

        // EO: arrange

        // act
        toTest.registerAndLogin(
                testUserRegistrationDTO,
                mockLocaleResolver,
                dummyAuthenticationConsumerStub
        );
        // EO: act

        // assert
        verify(mockUserRepository).save(userEntityArgumentCaptor.capture());

        UserEntity actualSavedUser = userEntityArgumentCaptor.getValue();

        assertAll(
                () -> assertEquals(actualSavedUser.getUsername(), testReturnedUserEntity.getUsername(),
                        "Expect usernames to match"),
                () -> assertEquals(actualSavedUser.getEmail(), testReturnedUserEntity.getEmail(),
                        "Expect emails to match"),

                () -> assertEquals(actualSavedUser.getPassword(), testReturnedUserEntity.getPassword(),
                        "Expect passwords to match"),

                () -> assertEquals(actualSavedUser.getFirstName(), testReturnedUserEntity.getFirstName(),
                        "Expect first name to match"),
                () -> assertNull(actualSavedUser.getIsActive(),
                        "Expected active property not to be set yet"),

                () -> verify(mockEmailService, times(1)).sendRegistrationEmail(
                        testEmail,
                        testFirstName + " " + testLastName,
                        mockLocaleResolver
                )
        );
        // EO: assert
    }
}
