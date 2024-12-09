package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.mapper.event.GoalCompletedEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.event.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GoalCompletedEventListener extends AbstractListenerForAddEvent<GoalCompletedEvent> {
    private final GoalCompletedEventMapper goalCompletedEventMapper;
    private final AnalyticsEventService analyticsEventService;
    // тут ну такое, я вроде попытался вынести общею логику в абстрактный класс но как будто меньше кода не стало
    @Autowired
    public GoalCompletedEventListener(ObjectMapper objectMapper, GoalCompletedEventMapper goalCompletedEventMapper, AnalyticsEventService analyticsEventService) {
        super(objectMapper);
        this.goalCompletedEventMapper = goalCompletedEventMapper;
        this.analyticsEventService = analyticsEventService;
    }

    @Override
    public Class getEventClass() {
        return GoalCompletedEvent.class;
    }

    @Override
    public void handleEvent(GoalCompletedEvent goalCompletedEvent) {
        AnalyticsEvent analyticsEvent = goalCompletedEventMapper.toEntity(goalCompletedEvent);
        analyticsEvent.setEventType(EventType.GOAL_COMPLETED);
        analyticsEventService.addNewEvent(analyticsEvent);
    }
}
