package faang.school.analytics.service;

import faang.school.analytics.dto.analytics.AnalyticsEventDto;
import faang.school.analytics.dto.analytics.Interval;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalyticsEvents(long receiverId, EventType eventType, Interval interval) {
        List<AnalyticsEvent> analyticsEvents = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(analyticsEvent -> interval.getFrom().isBefore(analyticsEvent.getReceivedAt())
                                          && interval.getTo().isAfter(analyticsEvent.getReceivedAt()))
                .toList();
        return analyticsEventMapper.toDto(analyticsEvents);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalyticsEvents(long receiverId, EventType eventType, LocalDateTime from, LocalDateTime to) {
        List<AnalyticsEvent> analyticsEvents = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(analyticsEvent -> from.isBefore(analyticsEvent.getReceivedAt())
                                          && to.isAfter(analyticsEvent.getReceivedAt()))
                .toList();
        return analyticsEventMapper.toDto(analyticsEvents);
    }
}
