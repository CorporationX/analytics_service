package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.AnalyticsRequest;
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

    public List<AnalyticsEventDto> getAnalytics(AnalyticsRequest request) {

        analyticsValidator.validate(request);

        Interval interval = request.getInterval();

        return analyticsEventRepository
                .findByReceiverIdAndEventType(request.getReceiverId(), request.getType())
                .filter(interval == null ? periodFilter(request.getFrom(), request.getTo()) :
                        periodFilter(interval.getStart(), interval.getEnd()))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    private Predicate<AnalyticsEvent> periodFilter(LocalDateTime from, LocalDateTime to) {
        return event -> {
            LocalDateTime eventTime = event.getReceivedAt();
            return eventTime.isAfter(from) && eventTime.isBefore(to);
        };
    }
}
