package com.example.demo.gardenHealth.scheduler;

import com.example.demo.gardenHealth.service.GardenHealthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GardenHealthScheduler {

    private final GardenHealthService gardenHealthService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Ho_Chi_Minh")
    public void runDailyWriteHealth() {
        log.info("GardenHealthScheduler - start writeHealth() job");
        try {
            gardenHealthService.writeHealth();
            log.info("GardenHealthScheduler - writeHealth() finished successfully");
        } catch (Exception ex) {
            log.error("GardenHealthScheduler - writeHealth() failed", ex);

        }
    }
}
