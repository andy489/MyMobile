package com.soft.mobilele.service;

import com.soft.mobilele.mapper.MapStructMapper;
import com.soft.mobilele.model.dto.OfferAddDto;
import com.soft.mobilele.model.dto.OfferSearchDto;
import com.soft.mobilele.model.entity.ModelEntity;
import com.soft.mobilele.model.entity.OfferEntity;
import com.soft.mobilele.model.entity.UserEntity;
import com.soft.mobilele.model.entity.UserRoleEntity;
import com.soft.mobilele.model.enumerated.UserRoleEnum;
import com.soft.mobilele.model.view.OfferDetailsView;
import com.soft.mobilele.repository.OfferRepository;
import com.soft.mobilele.repository.OfferSpecification;
import com.soft.mobilele.service.monitoring.OffersSearchMonitoringService;
import jakarta.transaction.Transactional;
import org.hibernate.ObjectNotFoundException;
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

    OffersSearchMonitoringService offersSearchMonitoringService;

    @Autowired
    public OfferService(
            OfferRepository offerRepository,
            MapStructMapper mapper,
            UserService userService,
            ModelService modelService,
            BrandService brandService,
            OffersSearchMonitoringService offersSearchMonitoringService) {

        this.offerRepository = offerRepository;
        this.mapper = mapper;
        this.userService = userService;
        this.modelService = modelService;
        this.brandService = brandService;
        this.offersSearchMonitoringService = offersSearchMonitoringService;
    }

    public void addOffer(OfferAddDto offerAddDto, String username) {

        OfferEntity offerEntity = mapper.toEntity(offerAddDto);

        UserEntity userEntity = userService.getByUsername(username);
        ModelEntity modelEntity = modelService.getById(offerAddDto.getModelId());

        offerEntity.setModel(modelEntity);
        offerEntity.setSeller(userEntity);

        offerRepository.save(offerEntity);
    }

    public Page<OfferDetailsView> getOffers(OfferSearchDto offerSearchDto, Pageable pageRequest) {

        offersSearchMonitoringService.logOffersSearch();

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

    public OfferDetailsView getOfferDetails(String offerId, String viewerUsername) {

        OfferEntity offerEntity = offerRepository.findById(offerId)
                .orElseThrow(() -> new NoSuchElementException("Offer with id=" + offerId + " not found!"));

        OfferDetailsView detailsView = mapper.toDetailsView(offerEntity);
        detailsView.setViewerIsOwner(isOwner(offerEntity.getSeller().getId(), viewerUsername));

        return detailsView;
    }

    @Transactional
    public void deleteOffer(String offerId) {

        offerRepository.deleteById(offerId);
    }

    private Boolean isOwner(Long offerSellerId, String viewerUsername) {
        if (Objects.equals(viewerUsername, "")) {
            return false;
        }

        UserEntity viewerUserEntity = userService.getByUsername(viewerUsername);

        boolean isOwner = Objects.equals(offerSellerId, viewerUserEntity.getId());

        return isOwner || isAdmin(viewerUserEntity);
    }

    private Boolean isAdmin(UserEntity userEntity) {
        return userEntity.getUserRoles().stream()
                .map(UserRoleEntity::getUserRoleEnum)
                .anyMatch(r -> UserRoleEnum.ADMIN == r);
    }
}
