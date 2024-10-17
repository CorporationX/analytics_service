package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.service.PremiumBoughtEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import school.faang.user_service.model.dto.PremiumBoughtEventDto;

@Component
@Slf4j
@RequiredArgsConstructor
public class PremiumBoughtEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final PremiumBoughtEventHandler premiumBoughtEventHandler;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // Десериализация сообщения в PremiumBoughtEventDto
            PremiumBoughtEventDto event = objectMapper.readValue(message.getBody(), PremiumBoughtEventDto.class);

            // Обработка события
            premiumBoughtEventHandler.handlePremiumBoughtEvent(event);
        } catch (Exception e) {
            log.error("Failed to process PremiumBoughtEvent", e);
        }
    }
}