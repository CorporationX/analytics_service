package faang.school.analytics.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.kafka.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowerConsumer {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(
            topics = "follower-event",
            groupId = "follower-event-group",
            containerFactory = "objectContainerFactory"
    )
    public void consumeEvent(ConsumerRecord<String, Object> consumerRecord) {
        FollowerEvent followerEvent = objectMapper.convertValue(consumerRecord.value(), FollowerEvent.class);
        log.info("Получен новый followerEvent от пользователя с id: {}", followerEvent.followerId());
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        analyticsEvent.setAuthorId(followerEvent.followerId());
        if (followerEvent.projectId() == null) {
            analyticsEvent.setEventType(EventType.FOLLOWER_USER);
            analyticsEvent.setReceiverId(followerEvent.followeeId());
        } else {
            analyticsEvent.setEventType(EventType.FOLLOWER_PROJECT);
            analyticsEvent.setReceiverId(followerEvent.projectId());
        }
        analyticsEventService.saveEvent(analyticsEvent);
        log.info("Новый followerEvent от пользователя с id: {} сохранен в БД аналитики.", followerEvent.followerId());
    }
}
