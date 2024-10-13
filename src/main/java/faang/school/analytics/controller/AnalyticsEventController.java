package faang.school.analytics.controller;

import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.validator.AnalyticsEventValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/analytics")
@RequiredArgsConstructor
@Validated
public class AnalyticsEventController {

    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventValidator analyticsEventValidator;

    @GetMapping("/{receiverId}")
    public void getAnalytics(@PathVariable @Positive long receiverId,
                             @RequestParam @NotBlank String eventType,
                             @RequestParam(required = false) String interval,
                             @RequestParam(required = false) String startDate,
                             @RequestParam(required = false) String endDate) {

        analyticsEventValidator.intervalOrStartAndEndDate(interval, startDate, endDate);
        analyticsEventService.getAnalytics(receiverId, eventType, interval, startDate, endDate);
    }
}
