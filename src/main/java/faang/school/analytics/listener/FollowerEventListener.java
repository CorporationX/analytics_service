package faang.school.analytics.listener;

import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.service.FollowerEventService;
import org.springframework.stereotype.Component;

@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> {
    private final FollowerEventService followerEventService;

    public FollowerEventListener(FollowerEventService followerEventService) {
        super(FollowerEvent.class);
        this.followerEventService = followerEventService;
    }

    @Override
    protected void processEvent(FollowerEvent event) {
        followerEventService.save(event);
    }
}
