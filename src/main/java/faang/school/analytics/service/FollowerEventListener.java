package faang.school.analytics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.client.UserServiceClient;
import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.dto.UserDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FollowerEventListener extends AbstractListener<FollowerEvent> {

    private final AnalyticsEventMapper analyticsEventMapper;
    private final UserServiceClient userServiceClient;

    public FollowerEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                 AnalyticsEventService analyticsEventService, AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, analyticsEventService);
        this.analyticsEventMapper = analyticsEventMapper;
        this.userServiceClient = userServiceClient;
    }

    @Override
    protected FollowerEvent handleEvent(Message message) throws IOException {
        FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
        UserDto follower = userServiceClient.getUser(event.getFollowerId());
        UserDto followee = userServiceClient.getUser(event.getFolloweeId());
        System.out.println(follower.getUsername() + " has subscribed to " + followee.getUsername());
        return event;
    }

    @Override
    protected AnalyticsEvent mapToAnalyticsEvent(FollowerEvent event) {
        return analyticsEventMapper.toEntity(event);
    }

}
