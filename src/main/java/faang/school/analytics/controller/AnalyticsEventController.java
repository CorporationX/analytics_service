package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;

    @GetMapping("/analytics")
    public List<AnalyticsEventDto> getAnalytics(
            @RequestParam long receiverId, @RequestParam int eventTypeId,
            @RequestParam(required = false) String interval,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to) {
        return analyticsEventService.getAnalytics(receiverId,
                EventType.of(eventTypeId), interval != null ? Interval.parse(interval) : null, from, to);
    }
}
