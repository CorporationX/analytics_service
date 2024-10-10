package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.TimeInterval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static faang.school.analytics.model.TimeInterval.ALL;
import static faang.school.analytics.model.TimeInterval.DAY;
import static faang.school.analytics.model.TimeInterval.HOUR;
import static faang.school.analytics.model.TimeInterval.MINUTE;
import static faang.school.analytics.model.TimeInterval.MONTH;
import static faang.school.analytics.model.TimeInterval.SECOND;
import static faang.school.analytics.model.TimeInterval.WEEK;
import static faang.school.analytics.model.TimeInterval.YEAR;


@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private Map<TimeInterval, Function<LocalDateTime, LocalDateTime>> intervalConverter = new HashMap<>();

    @PostConstruct
    public void init() {
        intervalConverter.put(SECOND, time -> time.minusSeconds(1));
        intervalConverter.put(MINUTE, time -> time.minusMinutes(1));
        intervalConverter.put(HOUR, time -> time.minusHours(1));
        intervalConverter.put(DAY, time -> time.minusDays(1));
        intervalConverter.put(WEEK, time -> time.minusWeeks(1));
        intervalConverter.put(MONTH, time -> time.minusMonths(1));
        intervalConverter.put(YEAR, time -> time.minusYears(1));
    }

    @Transactional
    public AnalyticsEvent saveEvent(AnalyticsEvent event) {
        return analyticsEventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEvent> getAnalytics(long receiverId,
                                             EventType eventType,
                                             TimeInterval interval,
                                             LocalDateTime start,
                                             LocalDateTime end) {

        List<AnalyticsEvent> events = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);

        if (interval.equals(ALL)) {
            return events;
        }

        LocalDateTime startTime = Objects.nonNull(start) ? start : LocalDateTime.now();
        LocalDateTime endTime = Objects.nonNull(end) ? end : intervalConverter.get(interval).apply(startTime);

        return events.stream()
                .filter(event -> event.getReceivedAt().isAfter(startTime))
                .filter(event -> event.getReceivedAt().isBefore(endTime))
                .toList();
    }
}
