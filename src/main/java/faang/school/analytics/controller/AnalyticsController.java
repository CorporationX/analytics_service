package faang.school.analytics.controller;

import faang.school.analytics.dto.redis.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsEventService analyticsEventService;

    @GetMapping("/analytics/{id}")
    public List<AnalyticsEventDto> getAnalytics(@PathVariable("id") Long id,
                                                @RequestParam("eventType") String eventType,
                                                @RequestParam(value = "intervalType", required = false) String intervalType,
                                                @RequestParam(value = "period", required = false) String period
    ) {
        return analyticsEventService.getAnalytcs(id, eventType, intervalType, period);

    }
}
