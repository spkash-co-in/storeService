package com.example.demo.schedule;

import com.example.demo.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
public class RetireTradeScheduler {
    @Autowired private StoreService storeService;
    @Scheduled(fixedRate = 1000)
    public void scheduleFixedRateTask() {
        log.info("Retiring old trades from store");
        storeService.retireTrades();
    }
}
