package faang.school.analytics.listeners;

import faang.school.analytics.model.FundRaisedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FundRaisedEventListener {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @KafkaListener(topics = "${topic.fund-raised-event}",
            containerFactory = "kafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=faang.school.analytics.model.FundRaisedEvent"})
    public void onMessage(FundRaisedEvent event) {
        log.info("Fund raised event received: {}", event);
        analyticsEventService.saveEvent(analyticsEventMapper.toAnalyticsEvent(event));
        log.info("Fund raised event saved to database");
    }
}
