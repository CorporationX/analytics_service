package faang.school.analytics.eventlistner;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.RedisProperties;
import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoalCompletedEventListener extends AbstractEventListener {
    private final List<String> topicNameKeys = List.of("goal-complete");
    private final RedisProperties properties;
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService service;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            GoalCompletedEvent event = objectMapper.readValue(message.getBody(), GoalCompletedEvent.class);
            service.saveGoalCompleteEvent(event);
            log.info("Goal {} completion was saved, goalId: {}", event.goalName(), event.goalId());

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Analytics write exception");//todo make it custom on whole events refactor
        }
    }

    @Override
    public Set<ChannelTopic> getChannelTopics() {
        return super.getChanelTopics(topicNameKeys, properties);
    }
}
