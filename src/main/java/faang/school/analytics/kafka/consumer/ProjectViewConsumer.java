package faang.school.analytics.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.kafka.ProjectViewEvent;
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
public class ProjectViewConsumer {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(
            topics = "project-view",
            groupId = "project-view-group",
            containerFactory = "objectContainerFactory"
    )
    public void consumeEvent(ConsumerRecord<String, Object> consumerRecord) {
        ProjectViewEvent projectViewEvent = objectMapper.convertValue(consumerRecord.value(), ProjectViewEvent.class);
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        analyticsEvent.setReceiverId(projectViewEvent.projectId());
        analyticsEvent.setEventType(EventType.PROJECT_VIEW);
        analyticsEvent.setAuthorId(projectViewEvent.viewerId());
        analyticsEventService.saveEvent(analyticsEvent);
        log.info("Новый ProjectViewEvent c projectId: {} и viewerId: {} сохранен в БД Аналитики.",
                analyticsEvent.getReceiverId(),
                analyticsEvent.getAuthorId());
    }
}
