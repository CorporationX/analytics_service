package faang.school.analytics.controller;

import faang.school.analytics.model.EventType;
import faang.school.analytics.dto.Interval;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@Validated
public class AnalyticsEventController {

    private final AnalyticsEventService analyticsEventService;

    @PostMapping("/event")
    public ResponseEntity<Void> saveEvent(@RequestBody AnalyticsEventDto eventDto) {
        analyticsEventService.saveEvent(eventDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<AnalyticsEventDto>> getAnalytics(
            @RequestParam long receiverId,
            @RequestParam EventType eventType,
            @RequestParam(required = false) Interval interval,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to
    ) {
        List<AnalyticsEventDto> analytics = analyticsEventService.getAnalytics(receiverId,
                eventType,
                interval,
                from,
                to);
        return ResponseEntity.ok(analytics);
    }
}