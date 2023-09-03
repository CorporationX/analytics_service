package faang.school.analytics.messaging;

import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Data
public class PostViewEventConsumer {

    private final AnalyticsEventService service;

    @KafkaListener(topics = "post-view")
    public void listen(PostViewEvent event) {
        log.info("Received post-view event: {}", event);

        service.savePostEvent(event);
    }
}
