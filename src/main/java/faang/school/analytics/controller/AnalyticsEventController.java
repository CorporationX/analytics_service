package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.validation.Valid;
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

    private final AnalyticsEventService analyticsService;

    @PostMapping
    public ResponseEntity<String> saveEvent(@Valid @RequestBody AnalyticsEventDto eventDto) {
        analyticsService.saveEvent(eventDto);
        return ResponseEntity.ok("Analytics successful saved on database");
    }

    @PostMapping("/filter")
    public List<AnalyticsEventDto> getAnalytics(@Valid @RequestBody AnalyticsEventFilterDto filter) {
        return analyticsService.getAnalytics(filter);
    }
}
