package faang.school.analytics.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public AnalyticsEventDto saveEvent(AnalyticsEventDto analyticsEventDto) {
        AnalyticsEvent savedEvent = analyticsEventRepository.save(analyticsEventMapper.toEntity(analyticsEventDto));
        return analyticsEventMapper.toDto(savedEvent);
    }

    @Transactional
    public List<AnalyticsEventDto> getAnalytics(
            long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to) {
        
        LocalDateTime fromDate;
        LocalDateTime toDate;
        if (interval != null) {
            fromDate = interval.getStartDate();
            toDate = LocalDateTime.now();
        } else {
            fromDate = from;
            toDate = to;
        }

        List<AnalyticsEvent> analyticsEvents = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
            .filter(event -> event.getReceivedAt().isBefore(toDate) && event.getReceivedAt().isAfter(fromDate))
            .sorted((event1, event2) -> event1.getReceivedAt().compareTo(event2.getReceivedAt()))
            .toList();

        return analyticsEventMapper.toDtoList(analyticsEvents);
    }
}
