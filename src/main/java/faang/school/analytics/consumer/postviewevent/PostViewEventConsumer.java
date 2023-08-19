package faang.school.analytics.consumer.postviewevent;

import faang.school.analytics.dto.PostViewEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PostViewEventConsumer {

    @KafkaListener(topics = "post-view")
    public void listen(PostViewEvent event) {
        log.info("Received post-view event: {}", event);
    }
}
