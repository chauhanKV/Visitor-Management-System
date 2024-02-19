package io.bootify.my_gate_visitor_management_project.util;

import io.bootify.my_gate_visitor_management_project.repos.VisitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
// @EnableScheduling can be added to the main class of this project. This will detect any method decorated with @Scheduled to execute that in scheduled manner.
@EnableScheduling
public class VisitExpiredScheduler {

    private static Logger LOGGER = LoggerFactory.getLogger(VisitExpiredScheduler.class);

    @Autowired
    private VisitRepository visitRepository;

    @Scheduled(fixedDelay = 1000)
    public void SetExpired()
    {
        // Mark pending visits (rejected/unapproved for long time as expired
        LOGGER.info("Setting visit as expired");
    }

    // cron requires 6 parameters expression. Earlier I used 5 based on crontab.guru, it threw error.
    @Scheduled(cron = "0 0 15 * * *")
    public void SetExpiredAtCertainTime()
    {
        LOGGER.info("Scheduled to run -> At 00:00 on day-of-month 15.");
    }

}
