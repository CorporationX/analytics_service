package faang.school.analytics.service;

import faang.school.analytics.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.interval.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;

import faang.school.analytics.util.converter.AnalyticsParametersConverter;
import faang.school.analytics.validator.AnalyticsEventValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventValidator analyticsEventValidator;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsParametersConverter converter;

    @Override
    public AnalyticsEventDto save(AnalyticsEventDto analyticsEventDto) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(analyticsEventDto);
        return analyticsEventMapper.toDto(analyticsEventRepository.save(analyticsEvent));
    }

    @Override
    public List<AnalyticsEventDto> getAnalytics(Long receiverId, EventType eventType, String intervalType, Long intervalQuantity, LocalDate from, LocalDate to) {
        LocalDateTime fromTime = converter.convertDateTimeFrom(from).orElse(null);
        LocalDateTime toTime = converter.convertDateTimeTo(to).orElse(null);
        Interval interval = converter.convertInterval(intervalType, intervalQuantity);
        analyticsEventValidator.validateAnalyticsEventParams(interval, fromTime, toTime);
        if (interval != null) {
            toTime = LocalDateTime.now();
            fromTime = interval.getStartDateTime(toTime);
        }
        LocalDateTime startDateTime = fromTime;
        LocalDateTime endDateTime = toTime;
        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(event -> event.getReceivedAt().isBefore(endDateTime) || event.getReceivedAt().equals(endDateTime))
                .filter(event -> event.getReceivedAt().isAfter(startDateTime) || event.getReceivedAt().equals(startDateTime))
                .map(analyticsEventMapper::toDto)
                .sorted(Comparator.comparing(AnalyticsEventDto::getReceivedAt))
                .toList();
    }
}