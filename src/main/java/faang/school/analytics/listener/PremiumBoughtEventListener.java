package faang.school.analytics.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PremiumBoughtEventDto;
import faang.school.analytics.service.PremiumBoughtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class PremiumBoughtEventListener implements MessageListener {
    private final PremiumBoughtService premiumBoughtService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            PremiumBoughtEventDto premiumBoughtEventDto = objectMapper.readValue(message.getBody(),
                    PremiumBoughtEventDto.class);
            log.info("Received Premium Bought Event with user ID {}", premiumBoughtEventDto.getReceiverId());
            premiumBoughtService.saveEvent(premiumBoughtEventDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
