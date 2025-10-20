package com.ehdndqls.shuttle.schedule;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MidnightScheduledTask {
    DailyScheduleService dailyScheduleService;

    // 매일 00:00:00에 실행 (한국 시간)
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void runDailyAtMidnight() {
        try {
            // TODO: 자정에 실행할 작업 비즈니스서비스로직에서 구현

            System.out.println("자정 작업 실행 (Spring): " + java.time.ZonedDateTime.now(java.time.ZoneId.of("Asia/Seoul")));
        } catch (Exception e) {
            // 예외 처리/로깅
            e.printStackTrace();
        }
    }
}
