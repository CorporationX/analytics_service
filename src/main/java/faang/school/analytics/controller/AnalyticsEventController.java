package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalylticsEventDto;
import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsEventController {

    private final AnalyticsEventService service;

    @GetMapping("/list")
    public ResponseEntity<?> getAnalytics(@Valid @RequestBody AnalyticsFilterDto filters) {
        List<AnalylticsEventDto> events = service.getAnalytics(filters);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
}
