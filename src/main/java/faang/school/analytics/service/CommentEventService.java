package faang.school.analytics.service;

import faang.school.analytics.dto.CommentEvent;

public interface CommentEventService {
    void save(CommentEvent event);
}
