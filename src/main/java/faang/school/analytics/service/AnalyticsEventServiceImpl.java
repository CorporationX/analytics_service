package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;

    public void saveEvent(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save(analyticsEvent);
    }

    @Transactional(readOnly = true)
    public AnalyticsEvent getAnalyticOfEvent(Long entityId,
                                          Long eventTypeId,
                                          Long intervalId,
                                          String startDateTime,
                                          String endDateTime) {
        EventType eventType = EventType.of(eventTypeId.intValue());
        LocalDateTime startDate = getStartDate(intervalId, startDateTime, endDateTime);
        LocalDateTime endDate = getEndDate(intervalId, startDateTime, endDateTime);
        Stream<AnalyticsEvent> analyticsEventStream = analyticsEventRepository.findByReceiverIdAndEventType(entityId, eventType);
        return analyticsEventStream
                .filter(event -> !event.getReceivedAt().isBefore(startDate) || !event.getReceivedAt().isAfter(endDate))
                .findFirst()
                .orElseThrow(() -> new EntityExistsException("No analytics event found"));
    }

    private boolean checkFormatedDate(Long intervalId,
                                      String startDateTime,
                                      String endDateTime) {
        Interval interval = Interval.of(intervalId.intValue());
        if (interval == null) {
            if (startDateTime != null && endDateTime != null) {
                return false;
            } else {
                throw new IllegalArgumentException("The interval or 1 of the time markers is missing");
            }
        } else {
            return true;
        }
    }

    private LocalDateTime getStartDate(Long intervalId,
                                       String startDateTime,
                                       String endDateTime) {
        boolean check = checkFormatedDate(intervalId, startDateTime, endDateTime);
        if (check) {
            Interval interval = Interval.of(intervalId.intValue());
            return interval.getStartDateTime();
        } else {
            return LocalDateTime.parse(startDateTime);
        }
    }

    private LocalDateTime getEndDate(Long intervalId,
                                     String startDateTime,
                                     String endDateTime) {
        boolean check = checkFormatedDate(intervalId, startDateTime, endDateTime);
        if (check) {
            Interval interval = Interval.of(intervalId.intValue());
            return interval.getEndDateTime();
        } else {
            return LocalDateTime.parse(endDateTime);
        }
    }
}
