package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.RedisProperties;
import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.mapper.event.UserServiceEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEvent> {
    private final UserServiceEventMapper userServiceEventMapper;

    public GoalCompletedEventListener(AnalyticsEventService analyticsEventService,
                                      ObjectMapper objectMapper,
                                      UserServiceEventMapper userServiceEventMapper,
                                      RedisProperties redisProperties) {
        super(analyticsEventService, objectMapper, redisProperties);
        this.userServiceEventMapper = userServiceEventMapper;
    }

    @Override
    public List<String> getTopicNameKeys() {
        return List.of("goal-complete");
    }

    @Override
    protected Class<GoalCompletedEvent> getEventClass() {
        return GoalCompletedEvent.class;
    }

    @Override
    protected AnalyticsEvent mapToAnalyticsEvent(GoalCompletedEvent event) {
        return userServiceEventMapper.goalCompleteToAnalytics(event);
    }
}
