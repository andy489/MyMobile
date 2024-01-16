package com.soft.mobilele.model.dto;

import com.soft.mobilele.model.enumarated.EngineEnum;
import com.soft.mobilele.model.enumarated.TransmissionEnum;
import com.soft.mobilele.model.validation.offer.YearAfter1960AndNotFuture;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class OfferAddDto {

    @NotNull(message = "model is required")
    @Min(value = 1, message = "invalid model")
    private Long modelId;

    @NotNull(message = "price is required")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "invalid price")
    private String price;

    @NotNull(message = "engine type is required")
    private EngineEnum engineType;

    @NotNull(message = "transmission type is required")
    private TransmissionEnum transmissionType;

    @YearAfter1960AndNotFuture(message = "valid year after 1960 is required")
    private String year;

    @NotNull(message = "mileage is required")
    @PositiveOrZero(message = "valid positive or zero mileage is required")
    private Integer mileage;

    @NotBlank(message = "description is required")
    private String description;

//    @NotBlank(message = "not blank URL is required")
    @URL(message = "invalid URL")
    private String imageUrl;

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

}