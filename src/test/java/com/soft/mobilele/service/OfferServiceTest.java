package com.soft.mobilele.service;

import com.soft.mobilele.mapper.MapStructMapper;
import com.soft.mobilele.model.entity.BrandEntity;
import com.soft.mobilele.model.entity.ModelEntity;
import com.soft.mobilele.model.entity.OfferEntity;
import com.soft.mobilele.model.entity.UserEntity;
import com.soft.mobilele.model.entity.UserRoleEntity;
import com.soft.mobilele.model.enumarated.CategoryEnum;
import com.soft.mobilele.model.enumarated.EngineEnum;
import com.soft.mobilele.model.enumarated.TransmissionEnum;
import com.soft.mobilele.model.enumarated.UserRoleEnum;
import com.soft.mobilele.model.view.OfferDetailsView;
import com.soft.mobilele.repository.OfferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OfferServiceTest {

    @Mock
    private OfferRepository mockOfferRepository;

    @Mock
    private MapStructMapper mockMapper;

    @Mock
    private ModelService mockModelService;

    @Mock
    private BrandService mockBrandService;

    @Mock
    private UserService mockUserService;

    @InjectMocks
    private OfferService toTest;

    @Test
    void testGetOfferDetails() {
        final String testUUID = UUID.randomUUID().toString();

        final BrandEntity testBrandEntity = new BrandEntity()
                .setId(1L)
                .setName("TEST_BRAND");

        final ModelEntity testModelEntity = new ModelEntity()
                .setBrand(testBrandEntity)
                .setCategory(CategoryEnum.CAR)
                .setStartYear(2022)
                .setName("TEST_MODEL")
                .setId(2L);

        final UserRoleEntity testUserRoleEntity = new UserRoleEntity()
                .setId(2L)
                .setUserRoleEnum(UserRoleEnum.USER);

        final UserEntity testSeller = new UserEntity()
                .setId(3L)
                .setUsername("TEST_SELLER_USERNAME")
                .setFirstName("TEST_FIRST_NAME")
                .setLastName("TEST_SELLER_LAST_NAME")
                .setEmail("TEST_SELLER_EMAIL")
                .setImageUrl("TEST_SELLER_IMG_URL")
                .setIsActive(true)
                .setUserRoles(List.of(testUserRoleEntity));

        final OfferEntity testOfferEntity = new OfferEntity()
                .setId(testUUID)
                .setModel(testModelEntity)
                .setEngine(EngineEnum.GASOLINE)
                .setTransmission(TransmissionEnum.AT)
                .setMileage(30000)
                .setDescription("TEST_DESCRIPTION")
                .setImageUrl("TEST_IMG_URL")
                .setPrice(BigDecimal.valueOf(33000L))
                .setYear(2023)
                .setSeller(testSeller)
                .setCreated(dateProvider())
                .setModified(dateProvider().plusDays(3));

        final OfferDetailsView testOfferDetailsView = (OfferDetailsView) new OfferDetailsView()
                .setSellerFirstName(testOfferEntity.getSeller().getFirstName())
                .setSellerLastName(testOfferEntity.getSeller().getLastName())
                .setDescription(testOfferEntity.getDescription())
                .setId(testOfferEntity.getId())
                .setModel(testOfferEntity.getModel().getName())
                .setEngineType(testOfferEntity.getEngine().getDisplayName())
                .setTransmissionType(testOfferEntity.getTransmission().getDisplayName())
                .setMileage(testOfferEntity.getMileage())
                .setImageUrl(testOfferEntity.getImageUrl())
                .setPrice(testOfferEntity.getPrice().toString())
                .setYear(testOfferEntity.getYear())
                .setCreated(testOfferEntity.getCreated().toLocalDate());

        // arrange
        when(mockOfferRepository.findById(testUUID)).thenReturn(Optional.of(testOfferEntity));
        when(mockMapper.toDetailsView(testOfferEntity)).thenReturn(testOfferDetailsView);
        // EO: arrange

        // act
        OfferDetailsView offerDetailsView = toTest.getOfferDetails(testUUID);
        // EO: act

        // assert
        assertEquals(offerDetailsView.getSellerFullName(), testSeller.getFullName(),
                "Expects seller names to match");

        assertEquals("TEST_DESCRIPTION", offerDetailsView.getDescription(),
                "Expects offer description to match");
        // EO: assert
    }

    private LocalDateTime dateProvider() {
        return LocalDateTime.of(2022, Month.AUGUST, 1, 0, 0);
    }

}
