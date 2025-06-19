package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.RedisProperties;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.mapper.event.PostServiceEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LikeEventListener extends AbstractEventListener<LikeEvent> {
    private final PostServiceEventMapper postServiceEventMapper;

    public LikeEventListener(AnalyticsEventService analyticsEventService,
                             ObjectMapper objectMapper,
                             PostServiceEventMapper postServiceEventMapper,
                             RedisProperties redisProperties) {
        super(analyticsEventService, objectMapper, redisProperties);
        this.postServiceEventMapper = postServiceEventMapper;
    }

    @Override
    public List<String> getTopicNameKeys() {
        return List.of("like-event");
    }

    @Override
    protected Class<LikeEvent> getEventClass() {
        return LikeEvent.class;
    }

    @Override
    protected AnalyticsEvent mapToAnalyticsEvent(LikeEvent event) {
        return postServiceEventMapper.likeEventToAnalytics(event);
    }
}
