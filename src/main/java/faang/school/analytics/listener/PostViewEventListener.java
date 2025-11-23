package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostViewEventListener {
    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${kafka.consumers.post-view.topic}",
            containerFactory = "postViewConcurrentKafkaListenerContainerFactory"
    )

    public void consumePostViewEvent(PostViewEvent event, Acknowledgment ack) {
        log.info("RECEIVED PostViewEvent: postId={}, authorId={}, viewerId={}, time={}",
                event.postId(), event.authorId(), event.viewerId(), event.currentTime());

        try {
            analyticsEventService.processPostViewEvent(event);
            log.info("Successfully processed PostViewEvent for postId: {}", event.postId());

            ack.acknowledge();
            log.info("Acknowledged message for postId: {}", event.postId());
        } catch (Exception e) {
            log.error("Error processing PostViewEvent: {}", e.getMessage());
        }
    }
}
