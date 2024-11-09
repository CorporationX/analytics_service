package faang.school.analytics.service;

import faang.school.analytics.model.event.CommentEvent;

public interface AnalyticsCommentEventService {
    void saveCommentEvent(CommentEvent event);

}