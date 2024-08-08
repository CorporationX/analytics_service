package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional
    public AnalyticsEventDto saveEvent(AnalyticsEventDto analyticsEventDto) {
        AnalyticsEvent analyticsEvent = analyticsEventRepository.save(analyticsEventMapper.toEntity(analyticsEventDto));
        return analyticsEventMapper.toDto(analyticsEvent);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to) {
        Stream<AnalyticsEvent> analyticsEventStream = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
        if (interval != null) {
            analyticsEventStream = analyticsEventStream.filter(analyticsEvent -> isWithinInterval(analyticsEvent, interval));
        } else {
            analyticsEventStream = analyticsEventStream.filter(analyticsEvent -> isWithinDateRange(analyticsEvent, from, to));
        }
        return analyticsEventStream
            .sorted(this::compareAnalyticsEventByReceivedAt)
            .map(analyticsEventMapper::toDto)
            .toList();
    }

    private boolean isWithinInterval(AnalyticsEvent analyticsEvent, Interval interval) {
        LocalDateTime futureCutoff = LocalDateTime.now().plusDays(interval.getDays());
        return analyticsEvent.getReceivedAt().isBefore(futureCutoff);
    }

    private boolean isWithinDateRange(AnalyticsEvent analyticsEvent, LocalDateTime from, LocalDateTime to) {
        return analyticsEvent.getReceivedAt().isAfter(from) && analyticsEvent.getReceivedAt().isBefore(to);
    }

    private int compareAnalyticsEventByReceivedAt(AnalyticsEvent analyticsEvent, AnalyticsEvent otherAnalyticsEvent) {
        return analyticsEvent.getReceivedAt().compareTo(otherAnalyticsEvent.getReceivedAt());
    }
}
