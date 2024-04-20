package faang.school.analytics.controller;

import faang.school.analytics.dto.analytics.AnalyticsEventDto;
import faang.school.analytics.dto.analytics.Interval;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    public List<AnalyticsEventDto> getAnalyticsEvents(@RequestParam @Positive(message = "receiverId must be positive") long receiverId,
                                                      @RequestParam String eventType,
                                                      @RequestParam(required = false) String interval,
                                                      @RequestParam(required = false) String from,
                                                      @RequestParam(required = false) String to) {
        return getAnalytics(receiverId, eventType, interval, from, to);
    }

    private List<AnalyticsEventDto> getAnalytics(long receiverId, String eventType, String interval, String from, String to) {
        if (interval == null && (from == null || to == null)) {
            throw new DataValidationException("Request must have period to analytics get");
        }
        if (interval == null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
            LocalDateTime fromDate = LocalDateTime.parse(from, formatter);
            LocalDateTime toDate = LocalDateTime.parse(to, formatter);
            return analyticsService.getAnalyticsEvents(receiverId, EventType.of(eventType), fromDate, toDate);
        }
        return analyticsService.getAnalyticsEvents(receiverId, EventType.of(eventType), Interval.of(interval));
    }
}
