package com.mobilele.service;

import com.mobilele.model.user.MobileleUserDetails;
import com.mobilele.repository.UserRepository;
import com.mobilele.mapper.MapStructMapper;
import com.mobilele.model.dto.UserRegistrationDto;
import com.mobilele.model.entity.UserEntity;
import com.mobilele.model.entity.UserRoleEntity;
import com.mobilele.model.enumerated.UserRoleEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceRegistrationTest {

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
    private MailService mockMailService;

    @Mock
    private Locale mockLocaleResolver;

    @Captor
    private ArgumentCaptor<UserEntity> userEntityArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> activationToken;

    @Mock
    private UserService toTest;

    @Mock
    ApplicationEventPublisher appEventPublisher;


    @BeforeEach
    void setUp() {
        toTest = new UserService(
                mockUserRepository,
                mockPasswordEncoder,
                mockMapper,
                mockUserDetailsService,
                mockUserRoleService,
                mockMailService,
                appEventPublisher
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
        UserRegistrationDto testUserRegistrationDto = new UserRegistrationDto()
                .setUsername(testUsername)
                .setEmail(testEmail)
                .setFirstName(testFirstName)
                .setLastName(testLastName)
                .setPassword(testPassword)
                .setConfirmPassword(testConfirmPassword);

        UserEntity testUserEntity = new UserEntity()
                .setUsername(testUserRegistrationDto.getUsername())
                .setEmail(testUserRegistrationDto.getEmail())
                .setFirstName(testUserRegistrationDto.getFirstName())
                .setLastName(testUserRegistrationDto.getLastName())
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

        final String ENCODED_PASS = "ENCODED" + testUserRegistrationDto.getPassword() + "ENCODED";

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

        when(mockMapper.toEntity(testUserRegistrationDto))
                .thenReturn(testUserEntity);

        when(mockPasswordEncoder.encode(testUserRegistrationDto.getPassword()))
                .thenReturn(ENCODED_PASS);

        when(mockUserRoleService.getByUserRoleEnum(UserRoleEnum.USER))
                .thenReturn(testUserRoleEntity);

        when(mockUserRepository.save(testUserEntity)).
                thenReturn(testReturnedUserEntity);

        // bypassing strict stubbing
        lenient().when(mockUserDetailsService.loadUserByUsername(testReturnedUserEntity.getUsername()))
                .thenReturn(testUserDetails);

        // EO: arrange

        // act
        toTest.registerAndLogin(
                testUserRegistrationDto,
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
        UserRegistrationDto testUserRegistrationDto = new UserRegistrationDto()
                .setUsername(testUsername)
                .setEmail(testEmail)
                .setFirstName(testFirstName)
                .setLastName(testLastName)
                .setPassword(testPassword)
                .setConfirmPassword(testConfirmPassword);

        UserEntity testUserEntity = new UserEntity()
                .setUsername(testUserRegistrationDto.getUsername())
                .setEmail(testUserRegistrationDto.getEmail())
                .setFirstName(testUserRegistrationDto.getFirstName())
                .setLastName(testUserRegistrationDto.getLastName())
                .setImageUrl(testImageUrl);

        UserRoleEntity testUserRoleEntity = new UserRoleEntity()
                .setId(3L)
                .setUserRoleEnum(UserRoleEnum.USER);

        final String ENCODED_PASS = "ENCODED" + testUserRegistrationDto.getPassword() + "ENCODED";

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

        when(mockMapper.toEntity(testUserRegistrationDto))
                .thenReturn(testUserEntity);

        when(mockPasswordEncoder.encode(testUserRegistrationDto.getPassword()))
                .thenReturn(ENCODED_PASS);

        when(mockUserRoleService.getByUserRoleEnum(UserRoleEnum.USER))
                .thenReturn(testUserRoleEntity);

        when(mockUserRepository.save(testUserEntity)).
                thenReturn(testReturnedUserEntity);

        // bypassing strict stubbing
        lenient().when(mockUserDetailsService.loadUserByUsername(testReturnedUserEntity.getUsername()))
                .thenReturn(testUserDetails);

        // EO: arrange

        // act
        toTest.registerAndLogin(
                testUserRegistrationDto,
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
                        "Expected active property not to be set yet")
        );
        // EO: assert
    }
}
