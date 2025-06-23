package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.RedisProperties;
import faang.school.analytics.dto.RecommendationReceivedEvent;
import faang.school.analytics.mapper.event.UserServiceEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationReceivedEventListener extends AbstractEventListener {
    private final List<String> topicNameKeys = List.of("recommendation-event");
    private final RedisProperties properties;
    private final ObjectMapper objectMapper;
    private final UserServiceEventMapper eventMapper;
    private final AnalyticsEventService service;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RecommendationReceivedEvent eventDto = objectMapper.readValue(message.getBody(),
                    RecommendationReceivedEvent.class);
            AnalyticsEvent event = eventMapper.recommendationReceivedToAnalytics(eventDto);
            service.saveEvent(event);
            log.info("Recommendation received event was saved, eventId: {}", event.getId());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Set<ChannelTopic> getChannelTopics() {
        return super.getChannelTopics(topicNameKeys, properties);
    }
}
