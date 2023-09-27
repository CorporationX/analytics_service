package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticEventDto;
import faang.school.analytics.dto.AnalyticsIntervalDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.analytics.AnalyticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @GetMapping
    public List<AnalyticEventDto> getLatestEvents() {
        return analyticsService.getLatestEvents();
    }

    @PostMapping
    public void create(@RequestBody AnalyticEventDto analyticEventDto) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(analyticEventDto);
        analyticsService.create(analyticsEvent);
    }

    @GetMapping("/interval")
    public List<AnalyticEventDto> getAnalytics(@RequestBody @Valid AnalyticsIntervalDto analyticsIntervalDto) {
        return analyticsService.getAnalytics(analyticsIntervalDto);
    }
}
