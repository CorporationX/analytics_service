package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.util.EnumConvertor;
import faang.school.analytics.validator.AnalyticsEventValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventValidator analyticsEventValidator;

    @Transactional
    public AnalyticsEventDto saveEvent(AnalyticsEventDto analyticsEventDto) {
        log.info("Start saving analytics event");
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(analyticsEventDto);
        analyticsEvent.setReceivedAt(LocalDateTime.now());
        return analyticsEventMapper.toDto(analyticsEventRepository.save(analyticsEvent));
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(long receiverId, String eventType, String interval, LocalDateTime from, LocalDateTime to) throws IllegalAccessException {
        log.info("Start getting analytics event");
        LocalDateTime startDate;
        LocalDateTime endDate;
        Interval intervalTypeValue = EnumConvertor.convert(Interval.class, interval);
        EventType eventTypeValue = EnumConvertor.convert(EventType.class, eventType);

        if (intervalTypeValue != null) {
            startDate = intervalTypeValue.getFrom();
            endDate = intervalTypeValue.getTo();
        } else {
            analyticsEventValidator.validateDates(from, to);
            startDate = from;
            endDate = to;
        }
        System.out.println(startDate);
        System.out.println(endDate);
        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventTypeValue)
                .filter(event ->
                        event.getReceivedAt().isAfter(startDate) && event.getReceivedAt().isBefore(endDate))
                .map(analyticsEventMapper::toDto)
                .sorted(Comparator.comparing(AnalyticsEventDto::getReceivedAt).reversed())
                .toList();
    }
}
