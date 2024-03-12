package com.mymobile.model.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class ExchangeRatesDto {

    private String base;

    private Map<String, BigDecimal> rates;

}
