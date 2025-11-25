package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostViewEventListener implements AnalyticsEventListener<PostViewEvent> {

    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(
            topics = "${kafka.consumers.post-view.topic}",
            containerFactory = "postViewConcurrentKafkaListenerContainerFactory"
    )

    public void listen(PostViewEvent event,
                       Acknowledgment acknowledgment,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        log.info("Received PostViewEvent: partition={}, topic={}, event={}", partition, topic, event);

        try {
            processEvent(event);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Error processing PostViewEvent: partition={}, error={}", partition, e.getMessage(), e);
        }
    }

    @Override
    public void processEvent(PostViewEvent event) {
        analyticsEventService.processPostViewEvent(event);
    }
}