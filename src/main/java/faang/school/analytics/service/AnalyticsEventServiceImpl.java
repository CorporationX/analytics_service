package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validation.AnalyticsEventValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService{
    AnalyticsEventRepository analyticsEventRepository;
    AnalyticsEventMapper analyticsEventMapper;
    AnalyticsEventValidator analyticsEventValidator;

    public AnalyticsEventDto saveEvent(AnalyticsEvent analyticsEvent) {
        analyticsEventValidator.validateEventForSave(analyticsEvent);
        AnalyticsEvent savedAnalyticsEvent = analyticsEventRepository.save(analyticsEvent);
        return analyticsEventMapper.toAnalyticsEventDto(savedAnalyticsEvent);
    }

    public List<AnalyticsEventDto> getAnalytics(long receiverId,
                                                EventType eventType,
                                                Interval interval,
                                                LocalDateTime from,
                                                LocalDateTime to) {
        analyticsEventValidator.validateGetAnalyticsEventParams(receiverId,
                eventType,
                from,
                to,
                interval != null);
        Stream<AnalyticsEvent> analyticsEventStream =
                analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
        return analyticsEventStream
                .filter(analyticsEvent -> {
                    LocalDateTime receivedAt = analyticsEvent.getReceivedAt();
                    if (interval != null) {
                        return interval.isWithin(receivedAt);
                    }
                    return receivedAt.isAfter(from) && receivedAt.isBefore(to);
                })
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(analyticsEventMapper::toAnalyticsEventDto)
                .toList();
    }
}
