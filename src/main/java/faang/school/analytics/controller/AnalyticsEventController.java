package faang.school.analytics.controller;

import faang.school.analytics.dto.EventDto;
import faang.school.analytics.dto.RequestAnalyticsDto;
import faang.school.analytics.service.AnalyticsEventService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/analytics")
@RequiredArgsConstructor
@Validated
public class AnalyticsEventController {

    private final AnalyticsEventService analyticsEventService;

    @PostMapping
    public ResponseEntity<List<EventDto>> getEvents(@RequestBody @Valid RequestAnalyticsDto requestAnalyticsDto) {
        return ResponseEntity.ok(analyticsEventService.getAnalitics(requestAnalyticsDto));
    }
}
