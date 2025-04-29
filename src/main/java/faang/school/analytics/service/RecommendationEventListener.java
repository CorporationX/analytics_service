package faang.school.analytics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.RecommendationEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RecommendationEventListener implements MessageListener {

    private final AnalyticsEventService analyticsEventService;

    private final AnalyticsEventMapper analyticsEventMapper;

    private final ObjectMapper objectMapper;

    public static List<String> messageList = new ArrayList<>();

    @Override
    public void onMessage(Message message, byte[] pattern) {

        messageList.add(Arrays.toString(message.getBody()));
        log.info("Message received: {}", message);
        log.info("Pattern received: {}", new String(pattern, StandardCharsets.UTF_8));
        try {
            RecommendationEvent recommendationEvent = objectMapper.readValue(message.getBody(), RecommendationEvent.class);
            AnalyticsEventDto dto = analyticsEventService.saveEvent(analyticsEventMapper.fromRecommendationEventToDto(recommendationEvent));
            log.info("Event saved to database: {}", dto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
