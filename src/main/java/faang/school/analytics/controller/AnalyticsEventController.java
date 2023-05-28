package faang.school.analytics.controller;

import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.Interval;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnalyticsEventController {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private final AnalyticsEventService analyticsEventService;
    private final UserContext userContext;

    @GetMapping("/analytics")
    private List<AnalyticsEventDto> getProfileViewAnalytics(@RequestParam long receiverId,
                                                            @RequestParam int eventType,
                                                            @RequestParam(required = false) String interval,
                                                            @RequestParam(required = false) String from,
                                                            @RequestParam(required = false) String to) {
        if (userContext.getUserId() == receiverId) {
            EventType type = EventType.of(eventType);
            validateInterval(interval, from, to);
            LocalDateTime fromTime = null;
            LocalDateTime toTime = null;
            Interval actualInterval = null;
            try {
                if (from != null) {
                    fromTime = LocalDateTime.parse(from, FORMATTER);
                }
                if (to != null) {
                    toTime = LocalDateTime.parse(to, FORMATTER);
                }
                if (interval != null) {
                    actualInterval = Interval.valueOf(interval);
                }
            } catch (Exception e) {
                throw new DataValidationException("Bad request", "Invalid date/interval format");
            }
            return analyticsEventService.getAnalytics(receiverId, type, actualInterval, fromTime, toTime);
        } else {
            throw new DataValidationException("Bad request", "User can view only its own analytics");
        }
    }

    private void validateInterval(String interval, String from, String to) {
        if (interval != null && (from != null || to != null)) {
            throw new DataValidationException("Bad request", "Interval cannot be used with from or to parameters");
        }
        if (interval == null && (from == null || to == null)) {
            throw new DataValidationException("Bad request", "Interval or from and to parameters must be provided");
        }
    }
}
