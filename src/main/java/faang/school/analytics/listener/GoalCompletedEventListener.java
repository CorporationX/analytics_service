package faang.school.analytics.listener;

import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.service.GoalCompletedEventService;
import org.springframework.stereotype.Component;

@Component
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEvent> {
    private final GoalCompletedEventService goalCompletedEventService;
    public GoalCompletedEventListener(GoalCompletedEventService goalCompletedEventService) {
        super(GoalCompletedEvent.class);
        this.goalCompletedEventService = goalCompletedEventService;
    }

    @Override
    public void processEvent(GoalCompletedEvent event) {
        goalCompletedEventService.save(event);
    }
}
