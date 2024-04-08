package faang.school.analytics.controller;

import faang.school.analytics.dto.analytics.AnalyticsEventDto;
import faang.school.analytics.dto.analytics.Interval;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    public List<AnalyticsEventDto> getAnalyticsEvents(@RequestParam @Positive(message = "receiverId must be positive") long receiverId,
                                                      @RequestParam String eventType,
                                                      @RequestParam(required = false) String interval,
                                                      @RequestParam(required = false) LocalDateTime from,
                                                      @RequestParam(required = false) LocalDateTime to) {
        return getAnalytics(receiverId, eventType, interval, from, to);
    }

    private List<AnalyticsEventDto> getAnalytics(long receiverId, String eventType, String interval, LocalDateTime from, LocalDateTime to) {
        if (interval == null && (from == null || to == null)) {
            throw new DataValidationException("Request must have period to analytics get");
        }
        if (interval == null) {
            return analyticsService.getAnalytics(receiverId, EventType.of(eventType), from, to);
        }
        return analyticsService.getAnalytics(receiverId, EventType.of(eventType), Interval.of(interval));
    }
}
