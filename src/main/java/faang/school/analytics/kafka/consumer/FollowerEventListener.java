package faang.school.analytics.kafka.consumer;

import faang.school.analytics.event.FollowerEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FollowerEventListener {
    private final AnalyticsEventMapper mapper;
    private final AnalyticsEventService service;

    @KafkaListener(
            topics = "${spring.data.kafka.topics.follower-events.name}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(FollowerEvent event) {
        log.info(">>> [Listener] Received FollowerEvent: {}", event);
        var entity = mapper.toEntity(event);
        log.info(">>> [Listener] Mapped to AnalyticsEvent: {}", entity);
        service.save(entity);
        log.info(">>> [Listener] Saved AnalyticsEvent with id {}", entity.getId());
    }
}
