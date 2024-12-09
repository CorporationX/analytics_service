package faang.school.analytics.controller;

import faang.school.analytics.dto.analytic.AnalyticsEventDto;
import faang.school.analytics.service.events.AnalyticsEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsEventController {

    private final AnalyticsEventService analyticsEventService;

    @PostMapping
    public ResponseEntity<Void> saveAction(@RequestBody @Valid AnalyticsEventDto analyticsEventDto) {
        return analyticsEventService.saveAction(analyticsEventDto);
    }
}
