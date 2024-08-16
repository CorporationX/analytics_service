package faang.school.analytics.messaging.listener.mentorship;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.mentorship.MentorshipRequestEvent;
import faang.school.analytics.mapper.mentorship.MentorshipRequestEventMapper;
import faang.school.analytics.messaging.listener.AbstractEventListener;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.analytics.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MentorshipRequestedEventListener extends AbstractEventListener<MentorshipRequestEvent> implements MessageListener {

    private final MentorshipRequestEventMapper mentorshipRequestEventMapper;

    public MentorshipRequestedEventListener(ObjectMapper objectMapper,
                                    AnalyticsEventService analyticsEventService,
                                            MentorshipRequestEventMapper mentorshipRequestEventMapper) {
        super(objectMapper, analyticsEventService);
        this.mentorshipRequestEventMapper = mentorshipRequestEventMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, MentorshipRequestEvent.class, event -> {
            var analyticsEntity = mentorshipRequestEventMapper.toAnalyticsEntity(event);
            analyticsEntity.setEventType(EventType.MENTORSHIP_RECEIVED);
            persistAnalyticsData(analyticsEntity);
        });
    }
}