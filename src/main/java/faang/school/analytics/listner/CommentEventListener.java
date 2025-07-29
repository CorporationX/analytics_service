package faang.school.analytics.listner;

import faang.school.analytics.dto.AnalyticsDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentEventListener {

    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper mapper;

    @KafkaListener(
            topics = "${spring.kafka.topics.comment-topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaAnalyticsEventListener"
    )

    public void listenerEventStart(AnalyticsDto dto) {
        AnalyticsEvent event = mapper.toEntity(dto);

        if (event.getEventType() == null) {
            event.setEventType(EventType.POST_COMMENT);
        }
        analyticsEventService.processCommentEvent(event);
    }
}
