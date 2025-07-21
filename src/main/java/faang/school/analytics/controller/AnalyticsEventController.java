package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analytics")
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;

    @PostMapping("/comment-event")
    public void handleCommentEvent(@RequestBody AnalyticsEvent event) {
        AnalyticsDto analyticsEvent = AnalyticsEventMapper.toDto(event);
        analyticsEventService.processCommentEvent(event);
    }
}