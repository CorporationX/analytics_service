package faang.school.analytics.controller;

import faang.school.analytics.dto.AggregatedAnalyticDto;
import faang.school.analytics.dto.AnalyticsGetDto;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsEventController {

    private final AnalyticsEventService analyticsEventService;

    @PostMapping
    public ResponseEntity<List<AggregatedAnalyticDto>> getAnalytics(@RequestBody AnalyticsGetDto analyticsGetDto) {
        List<AggregatedAnalyticDto> aggregatedAnalyticDtos = analyticsEventService.getAnalytics(analyticsGetDto);
        return ResponseEntity.ok(aggregatedAnalyticDtos);
    }
}
