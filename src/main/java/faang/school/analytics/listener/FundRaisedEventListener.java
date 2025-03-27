package faang.school.analytics.listener;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.FundRaisedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FundRaisedEventListener {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @KafkaListener(topics = "${spring.kafka.consumer.fund-raised.topic}",
            containerFactory = "kafkaFundRaisedListenerContainerFactory")
    public void onMessage(FundRaisedEvent event) {
        log.info("Fund raised event received: {}", event);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
        AnalyticsEventDto savedEvent = analyticsEventService.saveEvent(analyticsEvent);
        log.info("Fund raised event with id {} saved to database", savedEvent.getId());
    }
}
