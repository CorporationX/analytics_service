package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
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

    @Transactional
    public void saveEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
        log.info("Saved event {} - {} ", event.getEventType(), event);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to) {

        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        if (interval == null) {
            startDateTime = from;
            endDateTime = to;
        } else {
            endDateTime = LocalDateTime.now();
            startDateTime = interval.getStartDataTime(interval, endDateTime);
        }
        log.info("Creating a list with parameters: receiverId-{}, eventType-{}, startDateTime-{}, endDateTime-{}",
                receiverId, eventType, startDateTime, endDateTime);
        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(event -> event.getReceivedAt().isAfter(startDateTime) || event.getReceivedAt().equals(startDateTime))
                .filter(event -> event.getReceivedAt().isBefore(endDateTime) || event.getReceivedAt().equals(endDateTime))
                .map(analyticsEventMapper::toDto)
                .toList();
    }
}