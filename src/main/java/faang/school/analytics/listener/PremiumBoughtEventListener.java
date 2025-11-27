package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PremiumBoughtEvent;
import faang.school.analytics.exeption.DeserializationException;
import faang.school.analytics.exeption.ValidationException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PremiumBoughtEventListener implements MessageListener {

    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String rawMessage = null;
        try {
            if (message == null) {
                log.error("Received null message");
                return;
            }

            byte[] messageBody = message.getBody();
            rawMessage = extractRawMessage(messageBody);
            
            if (messageBody == null) {
                log.error("Message body is null");
                return;
            }

            PremiumBoughtEvent event = objectMapper.readValue(messageBody, PremiumBoughtEvent.class);
            
            if (event == null) {
                log.error("Deserialized event is null. Raw message: {}", rawMessage);
                return;
            }

            log.info("Received PremiumBoughtEvent: userId={}, amount={}, duration={} months, dateTime={}",
                    event.getUserId(), event.getPaymentAmount(), event.getSubscriptionDurationMonths(),
                    event.getPurchaseDateTime());

            AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
            analyticsEventService.save(analyticsEvent);

            log.info("Successfully processed PremiumBoughtEvent for user: {}", event.getUserId());
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize PremiumBoughtEvent. Raw message: {}", rawMessage, e);
        } catch (DeserializationException e) {
            log.error("Failed to deserialize PremiumBoughtEvent. Raw message: {}", rawMessage, e);
        } catch (ValidationException e) {
            log.error("Invalid PremiumBoughtEvent data. Raw message: {}", rawMessage, e);
        } catch (Exception e) {
            log.error("Failed to process PremiumBoughtEvent. Raw message: {}", rawMessage, e);
        }
    }

    private String extractRawMessage(byte[] messageBody) {
        return messageBody != null ? new String(messageBody) : "null";
    }
}
