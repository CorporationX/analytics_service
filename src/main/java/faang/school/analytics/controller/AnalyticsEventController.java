package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics/")
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;

    @GetMapping("{receiverId}")
    public List<AnalyticsEventDto> getAnalytics(@PathVariable @NotNull long receiverId,
                                                @NotNull EventType eventType,
                                                @RequestParam(required = false) Interval interval,
                                                @RequestParam(required = false) LocalDateTime from,
                                                @RequestParam(required = false) LocalDateTime to) {
        return analyticsEventService.getAnalytics(receiverId, eventType, interval, from, to);
    }

}
