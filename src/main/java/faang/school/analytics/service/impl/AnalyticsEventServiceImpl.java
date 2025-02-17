package faang.school.analytics.service.impl;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.Interval;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void saveEvent(AnalyticsEvent event) {
        log.debug("Requested to save AnalyticsEvent with id = {}", event.getId());
        if (event.getEventType() == null) {
            throw new IllegalArgumentException("AnalyticsEvent must have not null field eventType");
        }
        if (event.getReceivedAt() == null) {
            throw new IllegalArgumentException("AnalyticsEvent must have not null field receivedAt");
        }
        analyticsEventRepository.save(event);
        log.info("Saved AnalyticsEvent with id = {}", event.getId());
    }

    @Override
    @Transactional
    public List<AnalyticsEventDto> getAnalytics(
            long receiverId,
            EventType eventType,
            Interval interval,
            LocalDateTime from,
            LocalDateTime to
    ) {
        log.debug("Requested to get AnalyticsEvents with parameters: receiverId = {}, eventType = {}, interval = {}," +
                " from = {}, to = {}", receiverId, eventType, interval, from, to);

        if (eventType == null) {
            throw new IllegalArgumentException("Event type must be not null");
        }
        LocalDateTime start;
        LocalDateTime end;
        if (interval != null) {
            end = LocalDateTime.now();
            start = interval.getStartTime(end);
        } else if (from != null && to != null) {
            start = from;
            end = to;
        } else {
            throw new IllegalArgumentException("Interval, from and to must be not null");
        }

        List<AnalyticsEventDto> result = analyticsEventRepository
                .findByReceiverIdAndEventTypeAndReceivedAtBetweenOrderByReceivedAtDesc(
                        receiverId, eventType, start, end
                )
                .map(analyticsEventMapper::toAnalyticsEventDto)
                .toList();
        log.debug("Found {} AnalyticsEvents", result.size());
        return result;
    }

}
