package faang.school.analytics.listener.post.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.events.post.view.PostViewEvent;
import faang.school.analytics.listener.EventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PostViewEventListener implements EventListener {

    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final ObjectMapper objectMapper;

    @Override
    @KafkaListener(topics = "${spring.kafka.topics.post-view-topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenEvent(String jsonEvent) {
        try {
            log.info("Json event: {}", jsonEvent);
            PostViewEvent postViewEvent = objectMapper.readValue(jsonEvent, PostViewEvent.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(postViewEvent);
            analyticsEventService.save(analyticsEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
