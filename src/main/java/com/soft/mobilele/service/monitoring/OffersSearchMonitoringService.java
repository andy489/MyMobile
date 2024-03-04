package com.soft.mobilele.service.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class OffersSearchMonitoringService {

    private final Counter offersSearch;


    public OffersSearchMonitoringService(MeterRegistry meterRegistry) {
        offersSearch = Counter.builder("offer_search_cnt")
                .description("How many offer searches have been performed")
                .register(meterRegistry);
    }

    public void logOffersSearch() {
        offersSearch.increment();
    }

}
