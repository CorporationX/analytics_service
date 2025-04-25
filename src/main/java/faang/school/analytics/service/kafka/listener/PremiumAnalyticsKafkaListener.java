package faang.school.analytics.service.kafka.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.premium.PremiumAnalyticsDto;
import faang.school.analytics.service.premium.PremiumAnalyticsService;
import faang.school.analytics.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static faang.school.analytics.messages.ErrorMessages.FAILED_TO_ACKNOWLEDGE_KAFKA_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PremiumAnalyticsKafkaListener {
    public static final String RECEIVED_MESSAGE_FROM_KAFKA = "Received message from kafka: {}";

    private final JsonUtils jsonUtils;
    private final PremiumAnalyticsService premiumAnalyticsService;

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics..premium-topic}",
            groupId = "${spring.kafka.consumer.groups.premium-group}"
    )
    @Transactional
    public void premiumAnalyticsListener(String message, Acknowledgment acknowledgment) {
        log.info(RECEIVED_MESSAGE_FROM_KAFKA, message);
        PremiumAnalyticsDto premiumAnalyticsDto = jsonUtils.deserialize(message, PremiumAnalyticsDto.class);
        premiumAnalyticsService.savePremiumAnalytics(premiumAnalyticsDto);
        acknowledgeMessage(acknowledgment);
    }

    private void acknowledgeMessage(Acknowledgment acknowledgment) {
        try {
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error(FAILED_TO_ACKNOWLEDGE_KAFKA_MESSAGE, e);
            throw new RuntimeException(e);
        }
    }
}
