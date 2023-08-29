package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.RecommendationEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RecommendationListener extends AbstractListener<RecommendationEventDto> {
    public RecommendationListener(ObjectMapper objectMapper,
                                  AnalyticsEventMapper analyticsEventMapper,
                                  AnalyticsEventRepository repository) {
        super(objectMapper, analyticsEventMapper, repository);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RecommendationEventDto event = readValue(message.getBody(), RecommendationEventDto.class);
        log.info("Received new recommendation event message: {}", event);
        save(analyticsEventMapper.toEntity(event));
    }
}
