package com.soft.mobilele.service;

import com.soft.mobilele.mapper.MapStructMapper;
import com.soft.mobilele.model.dto.OfferAddDTO;
import com.soft.mobilele.model.dto.OfferSearchDTO;
import com.soft.mobilele.model.entity.ModelEntity;
import com.soft.mobilele.model.entity.OfferEntity;
import com.soft.mobilele.model.entity.UserEntity;
import com.soft.mobilele.model.view.OfferDetailsView;
import com.soft.mobilele.repository.OfferRepository;
import com.soft.mobilele.repository.OfferSpecification;
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

    @Autowired
    public OfferService(
            OfferRepository offerRepository,
            MapStructMapper mapper,
            UserService userService,
            ModelService modelService,
            BrandService brandService
    ) {
        this.offerRepository = offerRepository;
        this.mapper = mapper;
        this.userService = userService;
        this.modelService = modelService;
        this.brandService = brandService;
    }

    public void addOffer(OfferAddDTO offerAddDto, String email) {

        OfferEntity offerEntity = mapper.toEntity(offerAddDto);

        UserEntity userEntity = userService.getByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("No such user (addOffer)"));
        ModelEntity modelEntity = modelService.getById(offerAddDto.getModelId())
                .orElseThrow(() -> new NoSuchElementException("No such model (addOffer)"));

        offerEntity.setModel(modelEntity);
        offerEntity.setSeller(userEntity);

        offerRepository.save(offerEntity);
    }

    public Page<OfferDetailsView> getOffers(OfferSearchDTO offerSearchDto, Pageable pageRequest) {

        int pageSize = offerSearchDto.getPerPage() == null ?
                pageRequest.getPageSize() : offerSearchDto.getPerPage().getSz();

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

    public OfferDetailsView getOfferDetails(String offerId) {

        OfferEntity offerEntity = offerRepository.findById(offerId)
                .orElseThrow(() -> new NoSuchElementException("No such offer (getOfferDetails)"));

        return mapper.toDetailsView(offerEntity);
    }
}
