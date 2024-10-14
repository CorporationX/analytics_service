package faang.school.analytics.service.impl.analyticsevent;

import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.dto.event.AnalyticsEventDto;
import faang.school.analytics.model.dto.event.AnalyticsEventFilterDto;
import faang.school.analytics.filter.analyticseventfilter.AnalyticsEventFilter;
import faang.school.analytics.mapper.analyticsevent.AnalyticsEventMapper;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.enums.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {
    private final List<AnalyticsEventFilter> analyticsEventFilters;
    private final AnalyticsEventMapper analyticEventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;

    @Override
    @Transactional
    public void saveEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
        log.info("Saved follower event: {}", event);
    }

    @Override
    @Transactional(readOnly = true)
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

