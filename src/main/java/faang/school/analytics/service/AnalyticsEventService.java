package faang.school.analytics.service;

import faang.school.analytics.event.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;

public interface AnalyticsEventService {
        AnalyticsEvent saveFollowerEvent(FollowerEvent followerEvent);
}
