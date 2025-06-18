package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.RecommendationEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventRepository repository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
           RecommendationEventDto dto = objectMapper.readValue(message.getBody(), RecommendationEventDto.class);
            log.info("Parsed json");
            AnalyticsEvent ev = AnalyticsEvent.builder()
                    .receiverId(dto.receiverId())
                    .actorId(dto.authorId())
                    .eventType(EventType.RECOMMENDATION_RECEIVED)
                    .receivedAt(dto.createdAt())
                    .build();
            repository.save(ev);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
