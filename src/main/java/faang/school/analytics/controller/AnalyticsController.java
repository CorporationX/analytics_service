package faang.school.analytics.controller;

import faang.school.analytics.dto.LocalDateTimeInput;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsService;
import faang.school.analytics.validator.AnalyticsControllerValidator;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {
    private final AnalyticsService service;
    private final AnalyticsControllerValidator validator;

    @GetMapping
    public List<AnalyticsEvent> getAnalytics(@RequestParam @Positive long receiverId,
                                             @RequestParam(required = false) String eventTypeString,
                                             @RequestParam(required = false) @Positive Integer eventTypeInteger,
                                             @RequestParam(required = false) String intervalString,
                                             @RequestParam(required = false) @Positive Integer intervalInteger,
                                             @RequestParam(required = false) LocalDateTimeInput startDate,
                                             @RequestParam(required = false) LocalDateTimeInput endDate) {
        validator.validateRequestParams(eventTypeString, eventTypeInteger, intervalString, intervalInteger, startDate, endDate);
        return service.getAnalytics(receiverId, eventTypeString, eventTypeInteger, intervalString, intervalInteger, startDate, endDate);
    }
}
