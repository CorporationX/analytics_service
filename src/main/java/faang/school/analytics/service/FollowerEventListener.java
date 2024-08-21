package faang.school.analytics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.client.UserServiceClient;
import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FollowerEventListener extends AbstractListener<FollowerEvent> {

    private final AnalyticsEventMapper analyticsEventMapper;

    public FollowerEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                 AnalyticsEventService analyticsEventService, AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, analyticsEventService);
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    protected FollowerEvent handleEvent(Message message) throws IOException {
        FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
        System.out.println(event.getFollowerId() + " has subscribed to " + event.getFolloweeId());
        return event;
    }

    @Override
    protected AnalyticsEvent mapToAnalyticsEvent(FollowerEvent event) {
        return analyticsEventMapper.toEntity(event);
    }

}
