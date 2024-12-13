package faang.school.analytics.redis.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.mentorship.MentorshipRequestedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.redis.listener.AbstractEventListener;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class MentorshipRequestedEventListener extends AbstractEventListener<MentorshipRequestedEvent> {

    @Value("${spring.data.redis.channel.mentorship.requested}")
    private String mentorshipRequestedChannel;

    private final AnalyticsEventMapper analyticsEventMapper;

    public MentorshipRequestedEventListener(
            ObjectMapper objectMapper,
            AnalyticsEventService analyticsEventService,
            AnalyticsEventMapper analyticsEventMapper
    ) {
        super(objectMapper, analyticsEventService);
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipRequestedEvent event = mapMessage(message, MentorshipRequestedEvent.class);
        save(analyticsEventMapper.mentorshipRequestedEventToAnalyticsEvent(event));
    }

    @Override
    public ChannelTopic getChannelTopic() {
        return new ChannelTopic(mentorshipRequestedChannel);
    }
}
