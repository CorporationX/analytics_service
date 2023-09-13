package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.MentorshipRequestEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;


@Component
public class MentorshipRequestedEventListener extends AbstractEventListener<MentorshipRequestEvent> implements MessageListener {
    private final AnalyticsEventMapper analyticsMapper;
    private final AnalyticsEventService analyticsEventService;

    @Autowired
    public MentorshipRequestedEventListener(ObjectMapper objectMapper, AnalyticsEventMapper analyticsMapper, AnalyticsEventService analyticsEventService) {
        super(objectMapper);
        this.analyticsMapper = analyticsMapper;
        this.analyticsEventService = analyticsEventService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipRequestEvent event = convertJsonToString(message, MentorshipRequestEvent.class);
        AnalyticsEvent analyticsEvent = analyticsMapper.toEntity(event);
        analyticsEvent.setEventType(EventType.MENTORSHIP_REQUEST);
        analyticsEventService.create(analyticsEvent);
    }
}
