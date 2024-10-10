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
import java.util.function.Supplier;

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
    private Map<TimeInterval, Supplier<LocalDateTime>> intervalConverter = new HashMap<>();

    @PostConstruct
    public void init() {
        intervalConverter.put(SECOND, () -> LocalDateTime.now().minusSeconds(1));
        intervalConverter.put(MINUTE, () -> LocalDateTime.now().minusMinutes(1));
        intervalConverter.put(HOUR, () -> LocalDateTime.now().minusHours(1));
        intervalConverter.put(DAY, () -> LocalDateTime.now().minusDays(1));
        intervalConverter.put(WEEK, () -> LocalDateTime.now().minusWeeks(1));
        intervalConverter.put(MONTH, () -> LocalDateTime.now().minusMonths(1));
        intervalConverter.put(YEAR, () -> LocalDateTime.now().minusYears(1));
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

        if (Objects.isNull(start) && Objects.isNull(end)) {
            return events.stream()
                    .filter(event -> event.getReceivedAt().isAfter(intervalConverter.get(interval).get()))
                    .toList();
        } else if (Objects.nonNull(start) && Objects.nonNull(end)) {
            if (start.isAfter(end)) {
                throw new IllegalArgumentException("start time can`t be after end time");
            }
        }


        if (Objects.nonNull(start)) {
            events = events.stream()
                    .filter(event -> event.getReceivedAt().isAfter(start))
                    .toList();
        }

        if (Objects.nonNull(end)) {
            events = events.stream()
                    .filter(event -> event.getReceivedAt().isBefore(end))
                    .toList();
        }

        return events;
    }
}
