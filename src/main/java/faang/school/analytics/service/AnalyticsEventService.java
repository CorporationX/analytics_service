package faang.school.analytics.service;

import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validator.analyticsEvent.AnalyticsEventValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventValidator analyticsEventValidator;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final UserContext userContext;

    @Transactional
    public List<AnalyticsEventDto> getAnalytics(Long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to) {
        analyticsEventValidator.allDateIntervalsAreEmpty(interval, from, to);

        if (interval == null) {
            analyticsEventValidator.customIntervalIsValid(from, to);
        }

        LocalDateTime startTime = (interval != null) ? interval.getStartTime() : from;
        LocalDateTime endTime = (interval != null) ? interval.getEndTime() : to;

        Stream<AnalyticsEvent> byReceiverIdAndEventType = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);

        return byReceiverIdAndEventType
                .filter(event -> event.getReceivedAt().isAfter(startTime) && event.getReceivedAt().isBefore(endTime))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    @Transactional
    public AnalyticsEventDto saveEvent(AnalyticsEventDto analyticsEvent) {
        AnalyticsEvent analyticsToSave = analyticsEventMapper.toEntity(analyticsEvent);

        return analyticsEventMapper.toDto(analyticsEventRepository.save(analyticsToSave));
    }

    public AnalyticsEventDto prepareAnalyticsToSave(AnalyticsEventDto analyticsEvent) {
        analyticsEvent.setActorId(userContext.getUserId());
        analyticsEvent.setActorId(analyticsEvent.getActorId() == null
                ? userContext.getUserId()
                : analyticsEvent.getActorId());
        analyticsEvent.setReceivedAt(analyticsEvent.getReceivedAt() == null
                ? LocalDateTime.now()
                : analyticsEvent.getReceivedAt());

        return analyticsEvent;
    }
}
