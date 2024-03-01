package faang.school.analytics.listener;

import faang.school.analytics.dto.event_broker.FollowerEvent;
import faang.school.analytics.service.EventService;
import org.springframework.stereotype.Component;

@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> {
    private final EventService eventService;

    public FollowerEventListener(EventService eventService) {
        super(FollowerEvent.class);
        this.eventService = eventService;
    }

    @Override
    protected void processEvent(FollowerEvent event) {
        eventService.saveFollower(event);
    }
}
