package faang.school.analytics.service;

import faang.school.analytics.dto.GoalCompletedEvent;

public interface AnalyticsEventService {
    void saveGoalCompleteEvent(GoalCompletedEvent event);
}