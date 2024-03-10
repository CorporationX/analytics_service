package faang.school.analytics.listener;

import faang.school.analytics.dto.MentorshipRequestedEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MentorshipRequestedEventListener extends AbstractEventListener<MentorshipRequestedEventDto> {
    @Autowired
    private AnalyticsEventService analyticsEventService;

    public MentorshipRequestedEventListener() {
        super(MentorshipRequestedEventDto.class);
    }

    @Override
    public void saveEvent(MentorshipRequestedEventDto event) {
        event.setEventType(EventType.MENTORSHIP_REQUEST);
        analyticsEventService.saveEvent(event);
    }
}
