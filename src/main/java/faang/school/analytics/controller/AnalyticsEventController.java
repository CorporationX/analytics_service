package faang.school.analytics.controller;

import faang.school.analytics.dto.AggregatedAnalyticDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.service.Interval;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsEventController {

    private final AnalyticsEventService analyticsEventService;

    @GetMapping("/{receiverId}/{eventType}/{interval}/{from}/{to}")
    public ResponseEntity<List<AggregatedAnalyticDto>> getAnalytics(
            @PathVariable Long receiverId,
            @PathVariable EventType eventType,
            @PathVariable(required = false) Interval interval,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        List<AggregatedAnalyticDto> aggregatedAnalyticDtos = analyticsEventService.getAnalytics(
                receiverId, eventType, interval, from, to
        );
        return ResponseEntity.ok(aggregatedAnalyticDtos);
    }
}
