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
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Builder
public class AnalyticsService {

    private final AnalyticsEventValidator analyticsEventValidator;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventIntervalFilter analyticsEventIntervalFilter;
    private final AnalyticsEventPeriodFilter analyticsEventPeriodFilter;

    public void saveEvent(AnalyticsEventDto analyticsEventDto) {
        AnalyticsEvent eventToSave = analyticsEventMapper.toEntity(analyticsEventDto);
        analyticsEventRepository.save(eventToSave);
    }

    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType,
                                                String intervalStr, String fromStr, String toStr) {
        analyticsEventValidator.validateTimeBoundsPresence(intervalStr, fromStr, toStr);
        Stream<AnalyticsEvent> analyticsEventStream = analyticsEventRepository
                .findByReceiverIdAndEventType(receiverId, eventType);
        analyticsEventStream = filterAnalyticsByTimeBounds(analyticsEventStream, intervalStr, fromStr, toStr);
        return analyticsEventStream.sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    private Stream<AnalyticsEvent> filterAnalyticsByTimeBounds(Stream<AnalyticsEvent> analyticsEventStream,
                                                               String intervalStr, String fromStr, String toStr) {
        if (intervalStr != null) {
            Interval interval = Interval.valueOf(intervalStr);
            return analyticsEventIntervalFilter.filter(analyticsEventStream, interval);
        }
        LocalDateTime from = LocalDateTime.parse(fromStr);
        LocalDateTime to = LocalDateTime.parse(toStr);
        return analyticsEventPeriodFilter.filter(analyticsEventStream, from, to);
    }
}