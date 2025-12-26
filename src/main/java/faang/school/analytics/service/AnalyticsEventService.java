package faang.school.analytics.service;

import faang.school.analytics.dto.analytics_event.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
@Service
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType,
                                                Interval interval, LocalDateTime from, LocalDateTime to) {

        List<AnalyticsEvent> events = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);

        final LocalDateTime fromFinal;
        final LocalDateTime toFinal;

        if (interval != null) {
            toFinal = LocalDateTime.now();
            fromFinal = switch (interval) {
                case DAY -> toFinal.minusDays(1);
                case WEEK -> toFinal.minusWeeks(1);
                case MONTH -> toFinal.minusMonths(1);
                case YEAR -> toFinal.minusYears(1);
            };
        } else {
            fromFinal = from;
            toFinal = to;
        }

        return events.stream()
                .filter(event ->
                        event.getReceivedAt().isAfter(fromFinal)
                                && event.getReceivedAt().isBefore(toFinal)
                )
                .map(analyticsEventMapper::toDto)
                .collect(Collectors.toList());
    }

    public void saveEvent(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save(analyticsEvent);
    }
}
