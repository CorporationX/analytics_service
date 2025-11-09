package faang.school.analytics.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CreateAnalyticsEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/events")
@Validated
@RequiredArgsConstructor
public class AnalyticsEventController {
    private final AnalyticsEventService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnalyticsEventDto saveEvent(@Valid @RequestBody CreateAnalyticsEventDto dto) {
        return service.saveEvent(dto); 
    }

    @GetMapping("/{receiverId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public List<AnalyticsEventDto> getAnalytics(@PathVariable long receiverId,
            @RequestParam EventType eventType,
            @RequestParam Interval interval,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to) {
        return service.getAnalytics(receiverId, eventType, interval, from, to);
    }
}
