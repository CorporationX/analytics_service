package faang.school.analytics.controller;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AnalyticController {

    private final AnalyticsEventServiceImpl analyticsEventService;

    @GetMapping("/analytic/{entityId}")
    public AnalyticsEvent getAnalytic(@PathVariable Long entityId,
                                      @RequestParam Long eventTypeId,
                                      @RequestParam(required = false) Long intervalId,
                                      @RequestParam(required = false) String startDateTime,
                                      @RequestParam(required = false) String endDateTime) {
        return analyticsEventService.getAnalyticOfEvent(entityId, eventTypeId, intervalId, startDateTime, endDateTime);
    }
}
