package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.util.Interval;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsEventService analyticsEventService;
    @GetMapping("/analytics")
    public List<AnalyticsEventDto> getAnalytics(@RequestParam("id") Long receiverId,
                                                @RequestParam("eventType") EventType eventType,
                                                @RequestParam(value = "interval", required = false) Interval interval,
                                                @RequestParam(value = "start", required = false) LocalDateTime from,
                                                @RequestParam(value = "end", required = false) LocalDateTime to){

        return analyticsEventService.getAnalytics(receiverId, eventType, interval, from, to);
    }
}
