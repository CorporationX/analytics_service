package faang.school.analytics.kafka.consumer;

import faang.school.analytics.kafka.events.PremiumBoughtEvent;
import faang.school.analytics.mapper.PremiumBoughtEventMapper;
import faang.school.analytics.model.AnalyticsPremiumBoughtEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.repository.AnalyticsPremiumBoughtEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class PremiumBoughtEventListener {

    private final PremiumBoughtEventMapper premiumBoughtEventMapper;
    private final AnalyticsPremiumBoughtEventRepository premiumBoughtEventRepository;

    @KafkaListener(
            topics = "${spring.kafka.topics.premium-bought-event-topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerPremiumBoughtEvent"
    )
    public void listenPremiumBoughtEvent(PremiumBoughtEvent event) {
        log.info("Received premium bought event DTO: {}", event);

        AnalyticsPremiumBoughtEvent entityToSave = premiumBoughtEventMapper.toAnalyticsPremiumBoughtEvent(event);

        premiumBoughtEventRepository.save(entityToSave);

        log.info("Successfully saved entity to DB with ID: {}", entityToSave.getId());
    }
}
