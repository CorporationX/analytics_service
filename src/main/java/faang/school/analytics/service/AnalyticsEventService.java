package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public AnalyticsEventDto saveEvent(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save(analyticsEvent);
        return analyticsEventMapper.toDto(analyticsEvent);
    }

    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to) {
        List<AnalyticsEvent> events;

        if (interval != null) {
            LocalDateTime start = interval.getStartTime();
            events = analyticsEventRepository.findByReceiverIdAndEventTypeAndAfterDate(receiverId, eventType, start);
        } else {
            events = analyticsEventRepository.findByReceiverIdAndEventTypeAndDateRange(receiverId, eventType, from, to);
        }

        return events.stream().map(analyticsEventMapper::toDto).collect(Collectors.toList());
    }

}
