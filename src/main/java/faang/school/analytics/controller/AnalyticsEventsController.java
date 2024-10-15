package faang.school.analytics.controller;


import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.event.type.AnalyticsEvent;
import faang.school.analytics.model.event.type.EventType;
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
    public List<AnalyticsEventDto> getAllAnalytics(@RequestParam Long receiverId,
                                                   @RequestParam EventType type) {
        List<AnalyticsEvent> events = analyticsService.getAllAnalytics(receiverId, type);
        return analyticsEventMapper.toAnalyticsEventDtoList(events);
    }

    @GetMapping("/interval")
    public List<AnalyticsEventDto> getAnalyticsInInterval(@RequestParam Long receiverId,
                                                          @RequestParam EventType type,
                                                          @RequestParam TimeInterval interval) {
        List<AnalyticsEvent> events = analyticsService.getAnalyticsInInterval(receiverId, type, interval);
        return analyticsEventMapper.toAnalyticsEventDtoList(events);
    }

    @GetMapping("/between-time")
    public List<AnalyticsEventDto> getAnalyticsBetweenInterval(@RequestParam Long receiverId,
                                                               @RequestParam EventType type,
                                                               @RequestParam LocalDateTime start,
                                                               @RequestParam LocalDateTime end) {
        List<AnalyticsEvent> events = analyticsService.getAnalyticsBetweenTime(receiverId, type, start, end);
        return analyticsEventMapper.toAnalyticsEventDtoList(events);
    }
}
