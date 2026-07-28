package org.example.moneylog.controller;

import lombok.RequiredArgsConstructor;
import org.example.moneylog.domain.User;
import org.example.moneylog.dto.ApiResponse;
import org.example.moneylog.dto.MonthlyStatisticsResponse;
import org.example.moneylog.service.StatisticsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/monthly")
    public ApiResponse<MonthlyStatisticsResponse> getMonthlyStatistics(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String yearMonth) {

        YearMonth targetMonth = (yearMonth != null) ? YearMonth.parse(yearMonth) : YearMonth.now();
        return ApiResponse.success("월별 통계를 조회했습니다.",
                statisticsService.getMonthlyStatistics(user.getId(), targetMonth));
    }
}
