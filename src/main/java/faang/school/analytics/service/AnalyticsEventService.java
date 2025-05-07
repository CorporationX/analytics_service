package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsRequestDto;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.enums.EventType;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.Interval;

import java.time.Instant;
import java.util.List;

public interface AnalyticsEventService {
    void saveCommentEvent(CommentEvent commentEvent);

    AnalyticsEventDto saveEvent(AnalyticsEvent event);

    List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval,
                                         Instant from, Instant to);

    List<AnalyticsEvent> getParseAnalytics(AnalyticsRequestDto requestDto);
}
