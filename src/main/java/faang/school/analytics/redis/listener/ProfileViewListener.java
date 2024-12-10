package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.redis.event.ProfileViewEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;

@Component
public class ProfileViewListener extends AbstractEventListener<ProfileViewEvent> {

    private final AnalyticsEventMapper analyticsEventMapper;

    @Value("${spring.data.redis.channel.profile-view}")
    private String channelName;

    public ProfileViewListener(ObjectMapper objectMapper,
                               AnalyticsEventService analyticsEventService,
                               AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, analyticsEventService);
        this.analyticsEventMapper = analyticsEventMapper;
    }

    public void onMessage(Message message, byte[] pattern) {
        ProfileViewEvent profileViewEvent = mapMessage(message, ProfileViewEvent.class);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.profileViewEventToAnalyticsEvent(profileViewEvent);
        save(analyticsEvent);
    }

    @Override
    public Topic getTopic() {
        return new ChannelTopic(channelName);
    }
}