package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analytics")
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @GetMapping
    public List<AnalyticsDto> getAllAnalytics() {
        List<AnalyticsEvent> events = analyticsEventService.getAllEvents();
        return events.stream()
                .map(analyticsEventMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/comment-event")
    public void handleCommentEvent(@Valid @RequestBody AnalyticsDto dto) {
        AnalyticsEvent event = analyticsEventMapper.toEntity(dto);
        analyticsEventService.processCommentEvent(event);
    }
}