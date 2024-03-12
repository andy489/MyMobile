package com.mymobile.service.sheduler;

import com.mymobile.service.UserActivationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ActivationLinkCleanUpScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivationLinkCleanUpScheduler.class);

    private final UserActivationService userActivationService;

    private final DateTimeFormatter dateTimeFormatter;

    public ActivationLinkCleanUpScheduler(UserActivationService userActivationService) {
        this.userActivationService = userActivationService;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, hh:mm:ss");
    }


    //    @Scheduled(cron = "*/10 * * * * *") // every 10 seconds
    @Scheduled(fixedRate = 100_000, initialDelay = 100_000) // every 100 seconds
    public void cleanUp() {

        LOGGER.info("Trigger cleanup of activation links. " + LocalDateTime.now().format(dateTimeFormatter));
        userActivationService.cleanUpObsoleteActivationLinks();
    }

}
