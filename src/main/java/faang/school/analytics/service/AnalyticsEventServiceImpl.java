package faang.school.analytics.service;

import faang.school.analytics.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.interval.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validator.AnalyticsEventValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventValidator analyticsEventValidator;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public AnalyticsEventDto save(AnalyticsEventDto analyticsEventDto) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(analyticsEventDto);
        return analyticsEventMapper.toDto(analyticsEventRepository.save(analyticsEvent));
    }

    @Override
    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to) {
        analyticsEventValidator.validateAnalyticsEventParams(interval, from, to);
        if (interval != null) {
            to = LocalDateTime.now();
            from = interval.getStartDateTime(to);
        }
        LocalDateTime startDateTime = from;
        LocalDateTime endDateTime = to;
        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(event -> event.getReceivedAt().isBefore(endDateTime) || event.getReceivedAt().equals(endDateTime))
                .filter(event -> event.getReceivedAt().isAfter(startDateTime) || event.getReceivedAt().equals(startDateTime))
                .map(analyticsEventMapper::toDto)
                .sorted(Comparator.comparing(AnalyticsEventDto::getReceivedAt))
                .toList();
    }
}