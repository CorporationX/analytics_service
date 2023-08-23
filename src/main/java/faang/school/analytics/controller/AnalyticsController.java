package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.analytics.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping
    public List<AnalyticEventDto> getLatestEvents() {
        return analyticsService.getLatestEvents();
    }
}
