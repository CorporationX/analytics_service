package faang.school.analytics.service.impl;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.service.Interval;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void saveEvent(AnalyticsEvent event) {
        checkDataBeforeSave(event);
        analyticsEventRepository.save(event);
        log.info("Analytic event save.");
    }

    @Override
    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval,
                                                LocalDateTime from, LocalDateTime to) {
        Stream<AnalyticsEvent> analyticsEventStream = analyticsEventRepository.findByReceiverIdAndEventType(receiverId,
                eventType);
        List<AnalyticsEvent> analyticsEvents = analyticsEventStream
                .filter(event -> checkDate(event, from, to, interval))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .toList();
        log.info("Found {} record's", analyticsEvents.size());
        return analyticsEventMapper.toDto(analyticsEvents);
    }

    private boolean checkDate(AnalyticsEvent event, LocalDateTime from, LocalDateTime to, Interval interval) {
        LocalDateTime receivedAt = event.getReceivedAt();
        LocalDateTime localDateTime = LocalDateTime.now();
        if (interval != null) {
            return interval.getStartDate(localDateTime).isBefore(receivedAt) && localDateTime.isAfter(receivedAt);
        } else {
            return from.isBefore(receivedAt) && to.isAfter(receivedAt);
        }
    }

    private void checkDataBeforeSave(AnalyticsEvent event) {
        if (event == null) {
            log.error("Analytics event can't be null.");
            throw new DataValidationException("Analytics event can't be null.");
        }
        if (event.getEventType() == null) {
            log.error("Event type can't be null.");
            throw new DataValidationException("Event type can't be null.");
        }
        if (event.getActorId() == 0) {
            log.error("Actor id can't be 0.");
            throw new DataValidationException("Actor id can't be 0.");
        }
        if (event.getReceiverId() == 0) {
            log.error("Receiver id can't be 0.");
            throw new DataValidationException("Receiver id can't be 0.");
        }
    }
}
