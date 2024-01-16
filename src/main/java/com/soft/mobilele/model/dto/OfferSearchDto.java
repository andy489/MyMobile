package com.soft.mobilele.model.dto;

import com.soft.mobilele.model.enumarated.EngineEnum;
import com.soft.mobilele.model.enumarated.SortBy;
import com.soft.mobilele.model.enumarated.TransmissionEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class OfferSearchDto {

    private String model;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private String descriptionInfix;

    private Integer yearFrom;

    private Integer yearTo;

    private Integer mileageFrom;

    private Integer mileageTo;

    private EngineEnum engineType;

    private TransmissionEnum transmissionType;

    // Additional

    private Boolean awd = false;

    private Boolean climatic = false;

    private Boolean parktronic = false;

    private Boolean navi = false;

    private Boolean leasing = false;

    private Boolean xenonLights = false;

    private Boolean isofixSystem = false;

    private Boolean antiBlockingSystem = false;

    private Boolean cabriolet = false;

    private Boolean bluetooth = false;

    // Pageable

    private Integer size;

    // Sort

    private Sort.Direction sortDirection;

    private SortBy sortBy;

    public int getAttributesCount() {
        return getClass().getDeclaredFields().length;
    }
}
