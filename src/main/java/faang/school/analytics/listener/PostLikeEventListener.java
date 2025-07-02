package faang.school.analytics.listener;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostLikeEventListener {

    private final EventListener eventListener;

    @KafkaListener(topics = "${kafka.topics.like}", groupId = "${kafka.groups.analytic}")
    public void consume(AnalyticsEventDto event) {
        eventListener.saveEvent(event, EventType.POST_LIKE);
    }
}
