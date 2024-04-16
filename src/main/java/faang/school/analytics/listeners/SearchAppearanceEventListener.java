package faang.school.analytics.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.SearchAppearanceEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SearchAppearanceEventListener implements MessageListener {

    private ObjectMapper objectMapper;
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("New message received: {}", message);
            SearchAppearanceEventDto eventDto = objectMapper.readValue(message.getBody(), SearchAppearanceEventDto.class);
        } catch (IOException e) {
            log.error("Error while parsing message");
            throw new RuntimeException(e);
        }

    }
}
