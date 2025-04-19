package faang.school.analytics.service;

import faang.school.analytics.exceptions.InvalidRequestException;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.parser.AnalyticsRequestParser;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static faang.school.analytics.constants.Constants.MISSING_DATE_PARAMS;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventServiceImpl implements AnalyticsEventService {
    private final AnalyticsEventRepository eventRepository;
    private final AnalyticsRequestParser parser;

    @Override
    public List<AnalyticsEvent> getAnalytics(Long receiverId, String eventTypeRaw, String interval, String startDate,
                                             String endDate) {

        EventType eventType = parser.parseEventType(eventTypeRaw);
        LocalDateTime start;
        LocalDateTime end;

        if (StringUtils.hasText(interval)) {
            Interval parsedInterval = parser.parseInterval(interval);
            start = parsedInterval.getStartDate();
            end = parsedInterval.getEndDate();
        } else if (StringUtils.hasText(startDate) && StringUtils.hasText(endDate)) {
            start = parser.parseDate(startDate);
            end = parser.parseDate(endDate);
        } else {
            throw new InvalidRequestException(MISSING_DATE_PARAMS);
        }

        log.info("Fetching analytics for receiverId={}, eventType={}, start={}, end={}", receiverId, eventType, start,
                end);
        return eventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(event -> !event.getReceivedAt().isBefore(start) && !event.getReceivedAt().isAfter(end))
                .toList();
    }
}
