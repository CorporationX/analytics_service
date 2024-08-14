package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional
    public AnalyticsEvent saveEventEntity(AnalyticsEvent analyticsEvent) {
        return analyticsEventRepository.save(analyticsEvent);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(long receiverId,
                                                EventType eventType,
                                                Interval interval,
                                                LocalDateTime from,
                                                LocalDateTime to) {

        to = (interval != null || to == null) ? LocalDate.now().minusDays(1).atTime(LocalTime.MAX) : to;
        from = interval != null ? calculateFromDate(interval, to) : from;

        return analyticsEventRepository.findFilteredAndSortedEvents(receiverId, eventType, from, to)
                .stream()
                .map(analyticsEventMapper::toDto)
                .collect(Collectors.toList());
    }

    private LocalDateTime calculateFromDate(Interval interval, LocalDateTime to) {
        if (interval == Interval.ALL_TIME) {
            return LocalDateTime.MIN;
        }
        return to.minusDays(interval.getValue());
    }
}
