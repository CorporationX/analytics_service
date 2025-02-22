package faang.school.analytics.listener;

import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectViewEventListener {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @KafkaListener(topics = "${spring.kafka.consumer.project-view.topic}",
    containerFactory = "kafkaProjectViewListenerContainerFactory")
    public void projectViewEventListener(ProjectViewEvent projectViewEvent, Acknowledgment ack) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(projectViewEvent);
        analyticsEvent.setEventType(EventType.PROJECT_VIEW);
        analyticsEventService.saveEvent(analyticsEvent);
        ack.acknowledge();
    }
}