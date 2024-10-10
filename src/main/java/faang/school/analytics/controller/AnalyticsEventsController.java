package faang.school.analytics.controller;


import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.TimeInterval;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsEventsController {
    private final AnalyticsEventService analyticsService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @GetMapping
    public List<AnalyticsEventDto> getAnalytics(@RequestParam Long receiverId,
                                                @RequestParam EventType type,
                                                @RequestParam(defaultValue = "MONTH" ) TimeInterval interval,
                                                @RequestParam LocalDateTime startDate,
                                                @RequestParam LocalDateTime endDate) {
        List<AnalyticsEvent> events = analyticsService.getAnalytics(receiverId, type, interval, startDate, endDate);
        return analyticsEventMapper.toAnalyticsEventDtoList(events);
    }
}
