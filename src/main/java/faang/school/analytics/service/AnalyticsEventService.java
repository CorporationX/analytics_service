package faang.school.analytics.service;

import faang.school.analytics.redis.MentorshipRequestEvent;

public interface AnalyticsEventService {

    void saveAnalyticEvent(MentorshipRequestEvent mentorshipRequestEvent);
}
