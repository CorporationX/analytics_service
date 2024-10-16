package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.dto.PostViewEvent;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class PostViewEventListener extends AbstractRedisListener<PostViewEvent> {
    private final AnalyticsEventMapper mapper;

    public PostViewEventListener(ObjectMapper objectMapper, AnalyticsEventMapper mapper,
                                 AnalyticsEventServiceImpl analyticsEventServiceImpl) {
        super(objectMapper, analyticsEventServiceImpl);
        this.mapper = mapper;
    }

    public void onMessage(Message message, byte[] pattern) {
        handleEvent(PostViewEvent.class, message, mapper::fromPostViewToEntity);
    }
}
