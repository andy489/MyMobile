package com.mobilele.service.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OfferSearchMonitoringService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfferSearchMonitoringService.class);

    private final Counter offersSearch;

    public OfferSearchMonitoringService(MeterRegistry meterRegistry) {
        offersSearch = Counter.builder("offer_search_cnt")
                .description("How many offer searches have been performed")
                .register(meterRegistry);
    }

    public void logOffersSearch() {
        LOGGER.info("Offer search was performed.");
        offersSearch.increment();
    }

}
