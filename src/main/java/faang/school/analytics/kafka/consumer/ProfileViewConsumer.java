package faang.school.analytics.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.kafka.ProfileViewEvent;
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
public class ProfileViewConsumer {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(
            topics = "profile-view",
            groupId = "profile-view-group",
            containerFactory = "objectContainerFactory"
    )
    public void consumeEvent(ConsumerRecord<String, Object> consumerRecord) {
        try {
            ProfileViewEvent profileViewEvent = objectMapper.convertValue(consumerRecord.value(), ProfileViewEvent.class);
            log.info("Из Kafka получен новый ProfileViewEvent c viewerId: {} и profileOwnerId: {}.",
                    profileViewEvent.viewerId(), profileViewEvent.profileOwnerId());
            AnalyticsEvent analyticsEvent = createAnalyticsEvent(profileViewEvent);
            analyticsEventService.saveEvent(analyticsEvent);
            log.info("Новый ProfileViewEvent c viewerId: {} и profileOwnerId: {} записан БД Аналитики.",
                    profileViewEvent.viewerId(),
                    profileViewEvent.profileOwnerId());
        } catch (Exception e) {
            log.error("Не удалось преобразовать полученный из Kafka ConsumerRecord в ProfileViewEvent.");
        }
    }

    private AnalyticsEvent createAnalyticsEvent(ProfileViewEvent profileViewEvent) {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        analyticsEvent.setReceiverId(profileViewEvent.profileOwnerId());
        analyticsEvent.setAuthorId(profileViewEvent.viewerId());
        analyticsEvent.setEventType(EventType.FOLLOWER);
        return analyticsEvent;
    }
}
