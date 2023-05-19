package faang.school.analytics.controller;

import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.dto.Interval;
import faang.school.analytics.dto.UserProfileViewDto;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.service.UserProfileAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserProfileAnalyticsController {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private final UserProfileAnalyticsService userProfileAnalyticsService;
    private final UserContext userContext;

    @GetMapping("/user/{userId}/analytics/view")
    private List<UserProfileViewDto> getProfileViewAnalytics(@PathVariable long userId,
                                                             @RequestParam(required = false) String interval,
                                                             @RequestParam(required = false) String from,
                                                             @RequestParam(required = false) String to) {
        if (userContext.getUserId() == userId) {
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
            return userProfileAnalyticsService.getProfileViewAnalytics(userId, actualInterval, fromTime, toTime);
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
