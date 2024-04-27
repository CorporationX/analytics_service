package faang.school.analytics.service;

import faang.school.analytics.dto.analytics.AnalyticsEventDto;
import faang.school.analytics.dto.analytics.Interval;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval) {
        List<AnalyticsEvent> analyticsEvents = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(analyticsEvent -> isBetween(interval.getFrom(), analyticsEvent.getReceivedAt(), interval.getTo()))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .toList();
        return analyticsEventMapper.toDto(analyticsEvents);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, LocalDateTime from, LocalDateTime to) {
        List<AnalyticsEvent> analyticsEvents = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(analyticsEvent -> isBetween(from, analyticsEvent.getReceivedAt(), to))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .toList();
        return analyticsEventMapper.toDto(analyticsEvents);
    }

    @Transactional
    public AnalyticsEventDto saveEvent(AnalyticsEventDto analyticsEventDto) {
        AnalyticsEvent savedEvent = analyticsEventRepository.save(analyticsEventMapper.toEntity(analyticsEventDto));
        log.info("Event saved: {}", savedEvent);
        return analyticsEventMapper.toDto(savedEvent);
    }

    @Transactional
    public AnalyticsEvent saveEvent(AnalyticsEvent analyticsEvent) {
        AnalyticsEvent savedEvent = analyticsEventRepository.save(analyticsEvent);
        log.info("Event saved: {}", savedEvent);
        return savedEvent;
    }

    private boolean isBetween(LocalDateTime from, LocalDateTime date, LocalDateTime to) {
        return from.isBefore(date) && to.isAfter(date);
    }
}
