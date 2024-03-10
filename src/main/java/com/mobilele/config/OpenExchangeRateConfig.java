package com.mobilele.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "open.exchange.rates")
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class OpenExchangeRateConfig {

    private String appId;

    private List<String> symbols;

    private String host;

    private String schema;

    private String path;

    private Integer decimalPrecision;

    private Boolean enabled;

}
