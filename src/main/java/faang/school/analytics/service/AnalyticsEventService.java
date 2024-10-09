package faang.school.analytics.service;

import faang.school.analytics.model.LikeEvent;

public interface AnalyticsEventService {
    void saveLikeEvent(LikeEvent likeEvent);
}
