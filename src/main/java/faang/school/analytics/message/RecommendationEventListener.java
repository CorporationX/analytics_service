package faang.school.analytics.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.RecommendationEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationEventListener implements MessageListener {
    private ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
          RecommendationEventDto recommendationEventDto = objectMapper.readValue(message.getBody(), RecommendationEventDto.class);
          //TODO как только можно будет смержить с мастером можно будет сохранить аналитику
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Received message from channel {}: {}", message.getChannel(), message.getBody());
    }
}
