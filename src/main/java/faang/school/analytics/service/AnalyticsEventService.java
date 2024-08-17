package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
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
    private final AnalyticsEventMapper mapper;

    @Transactional
    public void saveEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType,
                                                Interval interval, LocalDateTime from,
                                                LocalDateTime to) {

        Stream<AnalyticsEvent> analyticsEventStream =
                analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);

        if (interval != null) {
            LocalDateTime now = LocalDateTime.now();
            from = Interval.getFromDate(interval);
            to = now;
        }

        final LocalDateTime startInclusive = from;
        final LocalDateTime endInclusive = to;

        return analyticsEventStream // Criteria API better ?
                .filter(
                        analyticsEvent -> !startInclusive.isAfter(analyticsEvent.getReceivedAt())
                                && !endInclusive.isBefore(analyticsEvent.getReceivedAt())
                )
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(mapper::toDto)
                .toList();
    }

}
