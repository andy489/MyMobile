package com.soft.mobilele.model.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class OfferView {

    protected String id;

    protected LocalDate created;

    protected String brand;

    protected String model;

    protected String price;

    protected Integer mileage;

    protected String engineType;

    protected String transmissionType;

    protected Integer year;

    protected String description;

    protected String imageUrl;

}
