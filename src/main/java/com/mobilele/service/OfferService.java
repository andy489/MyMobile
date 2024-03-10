package com.mobilele.service;

import com.mobilele.model.dto.OfferAddDto;
import com.mobilele.model.user.MobileleUserDetails;
import com.mobilele.model.view.OfferDetailsView;
import com.mobilele.mapper.MapStructMapper;
import com.mobilele.model.dto.OfferSearchDto;
import com.mobilele.model.entity.ModelEntity;
import com.mobilele.model.entity.OfferEntity;
import com.mobilele.model.entity.UserEntity;
import com.mobilele.model.entity.UserRoleEntity;
import com.mobilele.model.enumerated.UserRoleEnum;
import com.mobilele.repository.OfferRepository;
import com.mobilele.repository.OfferSpecification;
import com.mobilele.service.aop.WarnIfExecutionExceeds;
import com.mobilele.service.monitoring.OfferSearchMonitoringService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class OfferService {

    private final OfferRepository offerRepository;

    private final MapStructMapper mapper;

    private final UserService userService;

    private final ModelService modelService;

    private final BrandService brandService;

    OfferSearchMonitoringService offerSearchMonitoringService;

    @Autowired
    public OfferService(
            OfferRepository offerRepository,
            MapStructMapper mapper,
            UserService userService,
            ModelService modelService,
            BrandService brandService,
            OfferSearchMonitoringService offerSearchMonitoringService) {

        this.offerRepository = offerRepository;
        this.mapper = mapper;
        this.userService = userService;
        this.modelService = modelService;
        this.brandService = brandService;
        this.offerSearchMonitoringService = offerSearchMonitoringService;
    }

    public void addOffer(OfferAddDto offerAddDto, String username) {

        OfferEntity offerEntity = mapper.toEntity(offerAddDto);

        UserEntity userEntity = userService.getByUsername(username);
        ModelEntity modelEntity = modelService.getById(offerAddDto.getModelId());

        offerEntity.setModel(modelEntity);
        offerEntity.setSeller(userEntity);

        offerRepository.save(offerEntity);
    }

    @WarnIfExecutionExceeds(
            timeInMillis = 1_000L
    )
    public Page<OfferDetailsView> getOffers(OfferSearchDto offerSearchDto, Pageable pageRequest) {

        int pageSize = offerSearchDto.getSize() == null ?
                pageRequest.getPageSize() : offerSearchDto.getSize();

        Sort.Direction sortDirection = offerSearchDto.getSortDirection() == null ?
                Objects.requireNonNull(pageRequest.getSort().getOrderFor("price")).getDirection() :
                offerSearchDto.getSortDirection();

        String sortBy = offerSearchDto.getSortBy() == null ?
                "price" : offerSearchDto.getSortBy().getFieldName();

        PageRequest newPageRequest =
                PageRequest.of(pageRequest.getPageNumber(), pageSize, Sort.by(sortDirection, sortBy));

        return offerRepository
                .findAll(new OfferSpecification(offerSearchDto), newPageRequest)
                .map(mapper::toDetailsView);
    }

    @WarnIfExecutionExceeds(
            timeInMillis = 500L
    )
    public OfferDetailsView getOfferDetails(String offerId, MobileleUserDetails viewerDetails) {

        OfferEntity offerEntity = offerRepository.findById(offerId)
                .orElseThrow(() -> new NoSuchElementException("Offer with id=" + offerId + " not found!"));

        OfferDetailsView detailsView = mapper.toDetailsView(offerEntity);
        detailsView.setViewerIsOwner(isOwner(offerEntity, viewerDetails != null ? viewerDetails.getUsername() : null)
        );

        return detailsView;
    }

    @Transactional
    public void deleteOffer(String offerId) {

        offerRepository.deleteById(offerId);
    }

    public Boolean isOwner(String offerId, String username) {
        return isOwner(offerRepository.findById(offerId).orElse(null), username);
    }

    private boolean isOwner(OfferEntity offerEntity, String username) {
        if (offerEntity == null || username == null) {
            // anonymous users own no offers
            // missing offers are meaningless
            return false;
        }

        UserEntity viewerEntity = userService.getByUsername(username);

        if (isAdmin(viewerEntity)) {
            // all admins own all offers
            return true;
        }

        return Objects.equals(
                offerEntity.getSeller().getId(),
                viewerEntity.getId());
    }

    private Boolean isAdmin(UserEntity userEntity) {
        return userEntity.getUserRoles().stream()
                .map(UserRoleEntity::getUserRoleEnum)
                .anyMatch(r -> UserRoleEnum.ADMIN == r);
    }
}
