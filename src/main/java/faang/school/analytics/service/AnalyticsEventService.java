package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsRequest;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validation.AnalyticsValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
@Validated
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsValidator analyticsValidator;

    @Transactional
    public AnalyticsEventDto saveEvent(@Valid AnalyticsEventDto eventDto) {
        AnalyticsEvent event = analyticsEventMapper.toEntity(eventDto);
        AnalyticsEvent savedEvent = analyticsEventRepository.save(event);
        return analyticsEventMapper.toDto(savedEvent);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(AnalyticsRequest request) {

        analyticsValidator.validate(request);
        Interval interval = request.getInterval();
        LocalDateTime from;
        LocalDateTime to;

        if (interval != null) {
            LocalDateTime baseTime = LocalDateTime.now();
            from = interval.getStart(baseTime);
            to = interval.getEnd(baseTime);
        } else {
            from = request.getFrom();
            to = request.getTo();
        }

        return analyticsEventRepository
                .findByReceiverIdAndEventType(request.getReceiverId(), request.getType())
                .filter(periodFilter(from, to))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    private Predicate<AnalyticsEvent> periodFilter(LocalDateTime from, LocalDateTime to) {

        return event -> {
            LocalDateTime eventTime = event.getReceivedAt();
            return eventTime != null
                    && !eventTime.isBefore(from)
                    && !eventTime.isAfter(to);
        };
    }

}
