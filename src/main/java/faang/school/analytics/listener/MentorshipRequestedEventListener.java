package faang.school.analytics.listener;

import faang.school.analytics.dto.MentorshipRequestedEvent;
import faang.school.analytics.service.MentorshipRequestedEventService;
import org.springframework.stereotype.Component;

@Component
public class MentorshipRequestedEventListener extends AbstractEventListener<MentorshipRequestedEvent> {
    private final MentorshipRequestedEventService mentorshipRequestedEventService;

    public MentorshipRequestedEventListener(MentorshipRequestedEventService mentorshipRequestedEventService) {
        super(MentorshipRequestedEvent.class);
        this.mentorshipRequestedEventService = mentorshipRequestedEventService;
    }

    @Override
    protected void processEvent(MentorshipRequestedEvent event) {
        mentorshipRequestedEventService.save(event);
    }
}
