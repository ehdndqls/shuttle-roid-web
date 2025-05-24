package com.ehdndqls.shuttle.schedules;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailySchedulesService {
    private final DailySchedulesRepository dailySchedulesRepository;
}
