package com.soft.mobilele.model.enumerated;

import lombok.Getter;

@Getter
public enum SortBy {
    PRICE("Price", "price"),
    MILEAGE("Mileage", "mileage"),
    YEAR("Year", "year");

    private final String displayName;
    private final String fieldName;

    SortBy(String displayName, String fieldName) {
        this.displayName = displayName;
        this.fieldName = fieldName;
    }
}
