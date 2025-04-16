package faang.school.analytics.service;

import faang.school.analytics.dto.CommentEvent;

public interface AnalyticsEventService {
    void saveCommentEvent(CommentEvent commentEvent);
}
