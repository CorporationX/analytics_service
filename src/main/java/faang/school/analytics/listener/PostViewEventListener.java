package faang.school.analytics.listener;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.event.PostViewEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostViewEventListener {

    private final AnalyticsEventService analyticsEventService;

    public void onMessage(PostViewEvent event) {
        try {
            log.info("Processing PostViewEvent: {}", event);

            AnalyticsEventDto eventDto = new AnalyticsEventDto();
            eventDto.setReceiverId(event.getPostId());
            eventDto.setActorId(event.getViewerId());
            eventDto.setEventType(EventType.POST_VIEW);
            eventDto.setReceivedAt(event.getViewedAt());

            analyticsEventService.saveEvent(eventDto);

            log.info("PostViewEvent processed successfully: {}", eventDto);
        } catch (Exception e) {
            log.error("Failed to process PostViewEvent: {}", event, e);
        }
    }
}