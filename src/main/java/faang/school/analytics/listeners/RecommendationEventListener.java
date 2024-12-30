package faang.school.analytics.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsCreateEventDto;
import faang.school.analytics.dto.EventTypeDto;
import faang.school.analytics.dto.event.RecommendationEventDto;
import faang.school.analytics.service.AnalyticsService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecommendationEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsService analyticsService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RecommendationEventDto recommendationEventDto;

        try {
            recommendationEventDto = objectMapper.readValue(message.getBody(), RecommendationEventDto.class);

        } catch (IOException e) {
            throw new RuntimeException("Проблема преобразования сообщения из redis", e);
        }

        analyticsService.saveEvent(new AnalyticsCreateEventDto(
                recommendationEventDto.receiverId(),
                recommendationEventDto.authorId(),
                recommendationEventDto.createdAt(),
                EventTypeDto.RECOMMENDATION_RECEIVED
        ));
    }
}
