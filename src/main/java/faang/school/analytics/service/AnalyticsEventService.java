package faang.school.analytics.service;

import faang.school.analytics.dto.LikeEvent;

public interface AnalyticsEventService {
    void addLikeEvent(LikeEvent event);
}
