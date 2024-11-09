package faang.school.analytics.service.impl;

import faang.school.analytics.mapper.AnalyticsCommentEventMapper;
import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.enums.Interval;
import faang.school.analytics.model.event.CommentEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsCommentEventService;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsCommentEventServiceImpl implements AnalyticsCommentEventService {

    private final AnalyticsEventRepository repository;
    private final AnalyticsCommentEventMapper mapper;

    @Transactional(readOnly = true)
    public Stream<AnalyticsEvent> getAnalytics(long receiverId, EventType eventType) {
        return repository.findByReceiverIdAndEventType(receiverId, eventType);
    }
    @Override
    public void saveCommentEvent(CommentEvent event) {
        AnalyticsEvent analyticsEvent = mapper.toAnalyticsEvent(event);
        analyticsEvent.setEventType(EventType.POST_COMMENT);
        repository.save(analyticsEvent);
    }
}