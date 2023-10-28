package com.soft.mobilele.model.enumarated;

import lombok.Getter;

@Getter
public enum EngineEnum {
    GASOLINE("Gasoline"),
    DIESEL("Diesel"),
    ELECTRIC("Electric"),
    HYBRID("Hybrid");

    private final String displayName;

    EngineEnum(String displayName) {
        this.displayName = displayName;
    }
}
