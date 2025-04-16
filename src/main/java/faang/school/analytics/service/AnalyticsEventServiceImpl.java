package faang.school.analytics.service;

import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;
    @Override
    public void saveCommentEvent(CommentEvent commentEvent) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(commentEvent);
        analyticsEvent.setEventType(EventType.POST_COMMENT);
        analyticsEventRepository.save(analyticsEvent);
        log.info("Saved CommentEvent to AnalyticsEvent: id={}, userId={}, commentId={}",
                analyticsEvent.getId(), analyticsEvent.getActorId(), analyticsEvent.getCommentId());
    }
}
