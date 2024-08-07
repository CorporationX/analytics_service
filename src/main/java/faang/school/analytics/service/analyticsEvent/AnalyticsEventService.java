package faang.school.analytics.service.analyticsEvent;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.exception.ExceptionMessages;
import faang.school.analytics.mapper.analyticsEvent.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public AnalyticsEventDto saveEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
        return analyticsEventMapper.toDto(event);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(long receiverId,
                                                String eventString,
                                                String intervalString,
                                                String fromDateString,
                                                String toDateString) {
        EventType eventType = EventType.conversionToEventType(eventString);
        Interval interval = Interval.conversionToInterval(intervalString);

        if (interval == null) {
            LocalDateTime from = LocalDateTime.parse(fromDateString);
            LocalDateTime to = LocalDateTime.parse(toDateString);
            return filterByFromAndToAndSort(receiverId, eventType, from, to);
        } else {
            return filterByIntervalAndSort(receiverId, eventType, interval);
        }
    }

    private List<AnalyticsEventDto> sortedAllEvent(long receiverId,
                                                   EventType eventType) {
        return analyticsEventMapper.toListDto(analyticsEventRepository
                .findByReceiverIdAndEventType(receiverId, eventType)
                .sorted((e1, e2) -> e2.getReceivedAt().compareTo(e1.getReceivedAt()))
                .toList());
    }

    private List<AnalyticsEventDto> filterByFromAndToAndSort(long receiverId,
                                                             EventType eventType,
                                                             LocalDateTime from,
                                                             LocalDateTime to) {
        return analyticsEventMapper
                .toListDto(analyticsEventRepository
                        .findByReceiverIdAndEventTypeFromAndTo(receiverId, eventType, from, to));
    }

    private List<AnalyticsEventDto> filterByIntervalAndSort(long receiverId,
                                                            EventType eventType,
                                                            Interval interval) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start;
        switch (interval) {
            case DAY:
                start = now.minusDays(1);
                break;
            case WEEK:
                start = now.minusWeeks(1);
                break;
            case MONTH:
                start = now.minusMonths(1);
                break;
            case YEAR:
                start = now.minusYears(1);
                break;
            case ALL_TIME:
                return sortedAllEvent(receiverId, eventType);
            default:
                log.error(ExceptionMessages.INTERVAL_NOT_FOUND + interval);
                throw new IllegalArgumentException(ExceptionMessages.INTERVAL_NOT_FOUND + interval);
        }
        return filterByFromAndToAndSort(receiverId, eventType, start, LocalDateTime.now());
    }
}