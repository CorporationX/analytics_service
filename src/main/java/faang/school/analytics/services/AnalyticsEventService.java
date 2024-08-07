package faang.school.analytics.services;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.filters.timeManagment.Interval;
import faang.school.analytics.filters.timeManagment.TimeRange;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.services.utils.AnalyticsUtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {
    private final AnalyticsUtilService analyticsUtilService;
    private final AnalyticsEventMapper analyticsEventMapper;

    public AnalyticsEventDto saveEvent(AnalyticsEvent event) {
        return analyticsEventMapper.toDto(analyticsUtilService.saveEvent(event));
    }

    public List<AnalyticsEventDto> getAnalytics(Long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to) {
        TimeRange timeRange = new TimeRange();
        if (interval == null) {
            timeRange.setStart(from);
            timeRange.setEnd(to);
        } else {timeRange.setStart(interval.getStart());
            timeRange.setEnd(interval.getEnd());};
        log.info("Interval for filtering Analytics has been formed: from {} to {}", timeRange.getStart(), timeRange.getEnd());
        List<AnalyticsEvent> events = analyticsUtilService.getAnalytics(receiverId, eventType)
                .filter(analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(from) && analyticsEvent.getReceivedAt().isBefore(to))
                .toList();
        return events.stream().map(analyticsEventMapper::toDto).toList();
    }
}
