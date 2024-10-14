package faang.school.analytics.controller.analyticsevent;

import faang.school.analytics.model.dto.event.AnalyticsEventDto;
import faang.school.analytics.model.dto.event.AnalyticsEventFilterDto;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics-events")
@RequiredArgsConstructor
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;

    @GetMapping
    public List<AnalyticsEventDto> getAnalyticsEvents(@RequestParam("receiver-id") @Positive long receiverId,
                                                      @RequestParam("event-type") EventType eventType,
                                                      AnalyticsEventFilterDto filters) {
        return analyticsEventService.getAnalytics(receiverId, eventType,
                filters.interval(), filters.from(), filters.to());
    }
}
