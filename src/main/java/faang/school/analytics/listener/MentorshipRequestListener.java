package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.MentorshipRequestEvent;
import faang.school.analytics.mapper.MentorshipRequestMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MentorshipRequestListener extends AbstractEventListener<MentorshipRequestEvent> {
    @Autowired
    public MentorshipRequestListener(ObjectMapper objectMapper,
                                     AnalyticsEventService analyticsEventService,
                                     MentorshipRequestMapper mentorshipRequestMapper) {
        super(objectMapper, analyticsEventService, mentorshipRequestMapper);
    }

    @Override
    protected Class<MentorshipRequestEvent> getEventType() {
        return MentorshipRequestEvent.class;
    }
}
