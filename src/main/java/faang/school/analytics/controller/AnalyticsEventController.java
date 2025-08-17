package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.service.Interval;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsEventController {

    private final AnalyticsEventService analyticsEventService;

    @GetMapping("/receiver/{receiverId}/type/{eventType}")
    public List<AnalyticsEventDto> getAnalytics(
            @PathVariable long receiverId,
            @PathVariable EventType eventType,
            @RequestParam(required = false) Interval interval,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return analyticsEventService.getAnalytics(receiverId, eventType, interval, from, to);
    }
}

