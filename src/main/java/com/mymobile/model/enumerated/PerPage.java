package com.mymobile.model.enumerated;

import lombok.Getter;

@Getter
public enum PerPage {
    ONE(1),
    FIVE(5),
    TEN(10),
    TWENTY(20),
    FIFTY(50);

    private final Integer sz;

    PerPage(Integer sz) {
        this.sz = sz;
    }
}
