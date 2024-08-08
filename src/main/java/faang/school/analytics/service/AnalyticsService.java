package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.filter.AnalyticsEventIntervalFilter;
import faang.school.analytics.filter.AnalyticsEventPeriodFilter;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validator.AnalyticsEventValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsEventValidator analyticsEventValidator;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventPeriodFilter analyticsEventPeriodFilter;
    private final AnalyticsEventIntervalFilter analyticsEventIntervalFilter;

    public void saveEvent(AnalyticsEventDto analyticsEventDto) {
        AnalyticsEvent eventToSave = analyticsEventMapper.toEntity(analyticsEventDto);
        analyticsEventRepository.save(eventToSave);
    }

    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval,
                                                LocalDateTime from, LocalDateTime to) {
        analyticsEventValidator.validateTimeBoundsPresence(interval, from, to);
        Stream<AnalyticsEvent> analyticsEventStream = analyticsEventRepository
                .findByReceiverIdAndEventType(receiverId, eventType);
        if (interval != null) {
            analyticsEventStream = analyticsEventIntervalFilter.filter(analyticsEventStream, interval);
        } else {
            analyticsEventStream = analyticsEventPeriodFilter.filter(analyticsEventStream, from, to);
        }
        return analyticsEventStream.sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                .map(analyticsEventMapper::toDto)
                .toList();
    }
}