package faang.school.analytics.controller;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static faang.school.analytics.model.EventType.parseEventType;
import static faang.school.analytics.model.Interval.parseInterval;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticController {
    private final AnalyticService analyticService;

    @GetMapping
    public ResponseEntity<?> getAnalytics(
            @RequestParam(name = "receiverId") long receiverId,
            @RequestParam(name = "eventType") String eventType,
            @RequestParam(name = "interval", required = false) String interval,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate
    ) {
        try {
            EventType eventTypeEnum = parseEventType(eventType);
            Interval intervalEnum = parseInterval(interval);
            LocalDateTime startDateTime = (startDate != null) ? LocalDateTime.parse(startDate) : null;
            LocalDateTime endDateTime = (endDate != null) ? LocalDateTime.parse(endDate) : null;

            if (intervalEnum == Interval.UNKNOWN && (startDate == null || endDate == null)) {
                return ResponseEntity.badRequest().body("Either 'interval' or both 'startDate' and 'endDate' must be provided.");
            }

            List<AnalyticsEvent> analytics = analyticService.getAnalytics(receiverId, eventTypeEnum, intervalEnum, startDateTime, endDateTime);

            return ResponseEntity.ok(analytics);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid parameters: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}