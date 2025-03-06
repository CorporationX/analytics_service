package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validator.AnalyticsValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsValidator analyticsValidator;

    @Transactional
    public AnalyticsEventDto saveEvent(AnalyticsEvent event) {
        return analyticsEventMapper.toDto(analyticsEventRepository.save(event));
    }

    @Transactional
    public List<AnalyticsEventDto> getAnalytics(long receiverId,
                                                EventType type,
                                                Interval interval,
                                                LocalDateTime from,
                                                LocalDateTime to) {

        analyticsValidator.validateEventType(type);
        analyticsValidator.validateInterval(interval, from, to);

        List<AnalyticsEvent> analyticsEvents = analyticsEventRepository
                .findByReceiverIdAndEventType(receiverId, type)
                .toList();

        analyticsEvents = analyticsEvents.stream()
                .filter(interval == null ? periodFilter(from, to) :
                        periodFilter(interval.getStart(), interval.getEnd()))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .toList();

        return analyticsEvents.stream().map(analyticsEventMapper::toDto).toList();
    }

    private Predicate<AnalyticsEvent> periodFilter(LocalDateTime from, LocalDateTime to) {
        return event -> {
            LocalDateTime eventTime = event.getReceivedAt();
            return eventTime.isAfter(from) && eventTime.isBefore(to);
        };
    }
}
