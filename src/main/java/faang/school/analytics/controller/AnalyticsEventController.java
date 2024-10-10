package faang.school.analytics.controller;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.dto.analyticsEvent.AnalyticsRequestParams;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.service.AnalyticsRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsRequestService analyticsRequestService;

    @GetMapping("/get-analytics-event")
    public ResponseEntity<List<AnalyticsEventDto>> getAnalytics(
            @RequestParam long receiverId,
            @RequestParam String eventType,
            @RequestParam(required = false) String interval,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {

        AnalyticsRequestParams params = analyticsRequestService.processRequestParams(eventType, interval, from, to);
        List<AnalyticsEvent> analyticsEvents = analyticsEventService.getAnalytics(
                receiverId,
                params.getEventType(),
                params.getInterval(),
                params.getFrom(),
                params.getTo()
        );
        List<AnalyticsEventDto> analyticsDtos = analyticsEventMapper.toDtoList(analyticsEvents);
        return ResponseEntity.ok(analyticsDtos);
    }
}
