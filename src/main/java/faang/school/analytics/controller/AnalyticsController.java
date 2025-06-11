package faang.school.analytics.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventRequestDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @PostMapping
    public AnalyticsEventDto handleEvent(@RequestBody AnalyticsEventDto analyticsEventDto) {
        return analyticsService.saveEvent(analyticsEventDto);
    }

    @GetMapping
    public List<AnalyticsEventDto> getEvent(@RequestBody AnalyticsEventRequestDto analyticsEventRequestDto) {
        Interval interval = analyticsEventRequestDto.getInterval() == null ? null : Interval.valueOf(analyticsEventRequestDto.getInterval().toUpperCase());

        return analyticsService.getAnalytics(
            analyticsEventRequestDto.getReceiverId(), 
            EventType.valueOf(analyticsEventRequestDto.getEventType().toUpperCase()), 
            interval,
            LocalDateTime.parse(analyticsEventRequestDto.getFrom()), 
            LocalDateTime.parse(analyticsEventRequestDto.getTo())
        );
    }
}
