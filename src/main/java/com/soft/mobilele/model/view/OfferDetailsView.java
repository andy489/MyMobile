package com.soft.mobilele.model.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class OfferDetailsView {

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

    private LocalDate modified;

    private String sellerFirstName;

    private String sellerLastName;

    private Boolean awd;

    private Boolean climatic;

    private Boolean parktronic;

    private Boolean navi;

    private Boolean leasing;

    private Boolean xenonLights;

    private Boolean isofixSystem;

    private Boolean antiBlockingSystem;

    private Boolean cabriolet;

    private Boolean bluetooth;

    private Boolean viewerIsOwner;

    public String getSellerFullName() {
        StringBuilder sb = new StringBuilder();

        if (!Objects.equals(sellerFirstName, "")) {
            sb.append(sellerFirstName);
        }

        if (!sb.isEmpty()) {
            sb.append(" ");
        }

        if (sellerLastName != null) {
            sb.append(sellerLastName);
        }

        return sb.isEmpty() ? "Anonymous" : sb.toString();
    }

}
