package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    public List<AnalyticsEventDto> getAnalytics(@RequestParam long receiverId,
                                                @RequestParam String eventType,
                                                @RequestParam(required = false) String interval,
                                                @RequestParam(required = false)
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime from,
                                                @RequestParam(required = false)
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime to) {
        EventType type = EventType.getFromString(eventType);
        Interval intervalObj = Interval.getFromString(interval);
        return analyticsService.getAnalytics(receiverId, type, intervalObj, from, to);
    }
}
