package faang.school.analytics.listener.post.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.events.post.view.PostViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.post.PostEventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PostViewEventListener {

    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${spring.kafka.topics.post-viewed.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "manualAckFactory"
    )
    public void listenEvent(String jsonEvent, Acknowledgment acknowledgment) {
        try {
            log.info("Json event: {}", jsonEvent);
            PostViewEvent postViewEvent = objectMapper.readValue(jsonEvent, PostViewEvent.class);

            if (shouldSkip(postViewEvent)) {
                acknowledgment.acknowledge();
                return;
            }

            AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(postViewEvent);
            analyticsEventService.save(analyticsEvent);
            acknowledgment.acknowledge();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean shouldSkip(PostViewEvent event) {
        if (event.getPostEventType() != PostEventType.ANALYTICS) {
            log.debug("Skipping non-analytics event: {}", event);
            return true;
        }
        return false;
    }
}
