package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.MentorshipRequestEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class MentorshipRequestEventListener extends AbstractEventListener<MentorshipRequestEvent>
        implements MessageListener {

    public MentorshipRequestEventListener(ObjectMapper objectMapper,
                                          AnalyticsEventService analyticsEventService,
                                          AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, analyticsEventService, analyticsEventMapper, MentorshipRequestEvent.class);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        AnalyticsEvent analyticsEvent = handleMessage(message);
        analyticsEventService.save(analyticsEvent);
    }

    @Override
    public AnalyticsEvent toAnalyticsEvent(MentorshipRequestEvent event) {
        return analyticsEventMapper.toAnalyticsEvent(event);
    }
}
