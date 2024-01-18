package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.premium.PremiumEvent;
import faang.school.analytics.service.PremiumEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class PremiumEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final PremiumEventService premiumEventService;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            PremiumEvent premiumEvent = objectMapper.readValue(message.getBody(), PremiumEvent.class);
            premiumEventService.save(premiumEvent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
