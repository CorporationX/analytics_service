package faang.school.analytics.listner;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentEventListener {

    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(
            topics = "${spring.kafka.topics.comment-topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaAnalyticsEventListener"
    )
    public void listenerEventStart(AnalyticsEvent event) {
        analyticsEventService.processCommentEvent(event);
    }
}
