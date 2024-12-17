package com.isep.certification.config.services.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    @Scheduled(cron = "0 0 0 * * *") // Cron expression for running every day at midnight
    public void execute() {
        System.err.println("Scheduled task executed at: " + System.currentTimeMillis());
    }
}