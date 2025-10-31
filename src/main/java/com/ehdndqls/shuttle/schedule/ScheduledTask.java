package com.ehdndqls.shuttle.schedule;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTask {
    private final DailyScheduleService dailyScheduleService;

    // 5분마다 실행 (한국 시간)
    @Scheduled(cron = "0 */5 * * * *", zone = "Asia/Seoul")
    public void runEveryFiveMinutes() {
        try {
            dailyScheduleService.updateSchedule();

            System.out.println("자정 작업 실행 (Spring): " + java.time.ZonedDateTime.now(java.time.ZoneId.of("Asia/Seoul")));
        } catch (Exception e) {
            // 예외 처리로깅
            e.printStackTrace();
        }
    }
}
