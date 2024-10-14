package faang.school.analytics.service;

import faang.school.analytics.event.MentorshipRequestEvent;

public interface AnalyticsEventService {

    void saveAnalyticEvent(MentorshipRequestEvent mentorshipRequestEvent);
}
