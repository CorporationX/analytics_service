package faang.school.analytics.listener;

import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostViewEventListener {
    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(
            topics = "${kafka.consumers.post-view.topic}",
            containerFactory = "postViewConcurrentKafkaListenerContainerFactory"
    )
    public void consumePostViewEvent(PostViewEvent event) {
        log.info("Received PostViewEvent from Kafka: {}", event);

        try {
            analyticsEventService.processPostViewEvent(event);
            log.info("Successfully processed PostViewEvent for postId: {}", event.postId());
        } catch (Exception e) {
            log.error("Error processing PostViewEvent: {}", e.getMessage());
        }
    }
}
