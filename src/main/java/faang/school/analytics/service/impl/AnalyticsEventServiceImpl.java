package faang.school.analytics.service.impl;

import faang.school.analytics.dto.analyticsevent.AnalyticsEventDto;
import faang.school.analytics.dto.analyticsevent.AnalyticsEventFilterDto;
import faang.school.analytics.filter.analyticseventfilter.AnalyticsEventFilter;
import faang.school.analytics.mapper.analyticevent.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final List<AnalyticsEventFilter> analyticsEventFilters;
    private final AnalyticsEventMapper analyticEventMapper;

    @Override
    public void sendEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
    }

    @Override
    @Transactional
    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval,
                                                LocalDateTime from, LocalDateTime to) {
        AnalyticsEventFilterDto filters = AnalyticsEventFilterDto.builder()
                .interval(interval)
                .from(from)
                .to(to)
                .build();

        Stream<AnalyticsEvent> analyticsEvents = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
        return analyticsEventFilters.stream()
                .filter(filter -> filter.isApplicable(filters))
                .reduce(analyticsEvents, (stream, filter) -> filter.apply(stream, filters),
                        (newStream, oldStream) -> newStream)
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                .map(analyticEventMapper::toDto)
                .toList();
    }
}
