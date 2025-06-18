package faang.school.analytics.listener;

import java.nio.charset.StandardCharsets;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsService analyticsService;

    @Override
    public void onMessage(@SuppressWarnings("null") Message message, @Nullable byte[] pattern) {
        try {
            String jsonBody = new String(message.getBody(), StandardCharsets.UTF_8);
            AnalyticsEventDto msgObject = objectMapper.readValue(jsonBody, AnalyticsEventDto.class);
            analyticsService.saveEvent(msgObject);
            log.info("Received message: {}", msgObject);
        } catch (JsonMappingException e) {
            log.error("Error while mapping the structure of the message: {}", e.getMessage());
        } catch (JsonProcessingException e) {
            log.error("Error while working with received message {}.", e.getMessage());
            e.printStackTrace();
        }
    }

}
