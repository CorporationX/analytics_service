package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsDto;
import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsController {
    private final AnalyticsEventService analyticsService;

    @PostMapping("/get/filtered")
    public ResponseEntity<List<AnalyticsDto>> getAnalytics(@RequestBody @Valid AnalyticsFilterDto analyticsFilter) {
        return ResponseEntity.ok(analyticsService.getAnalytics(analyticsFilter));
    }
}
