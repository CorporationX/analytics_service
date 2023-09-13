package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;

    public void saveEvent(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save(analyticsEvent);
    }

    public AnalyticsDto getAnalytics(Long userId, Integer eventTypeNumber, String eventTypeName,
                                     String intervalName, List<String> dateNames) {
        validateForExceptions(eventTypeNumber, eventTypeName, intervalName, dateNames);
        EventType eventType = checkEventType(eventTypeNumber, eventTypeName);
        Interval interval = getInterval(intervalName);
        List<LocalDateTime> dates = getDates(dateNames);

        Stream<AnalyticsEvent> requiredEvents = null;
        LocalDateTime today = LocalDateTime.now();
        if (interval != null) {
            switch (interval) {
                case DAY -> requiredEvents = analyticsEventRepository.findOrderedAndFilteredByTimeInterval(
                        userId, eventType, today.minusDays(1), today);
                case WEEK -> requiredEvents = analyticsEventRepository.findOrderedAndFilteredByTimeInterval(
                        userId, eventType, today.minusDays(7), today);
                case MONTH -> requiredEvents = analyticsEventRepository.findOrderedAndFilteredByTimeInterval(
                        userId, eventType, today.minusMonths(1), today);
                case YEAR -> requiredEvents = analyticsEventRepository.findOrderedAndFilteredByTimeInterval(
                        userId, eventType, today.minusYears(1), today);
            }
        }
        if (dates != null) {
            LocalDateTime startDate = dates.get(0);
            LocalDateTime endDate = dates.get(1);
            requiredEvents = analyticsEventRepository.findOrderedAndFilteredByTimeInterval(
                    userId, eventType, startDate, endDate);
        }
        return new AnalyticsDto(userId, requiredEvents.map(AnalyticsEvent::getActorId).toList(), eventType);
    }

    private void validateForExceptions(Integer eventTypeNumber, String eventTypeName, String interval, List<String> dates){
        if(eventTypeName.isBlank() && eventTypeNumber == 0){
            throw new IllegalArgumentException("Select required event type");
        }
        if(!eventTypeName.isBlank() && eventTypeNumber > 0){
            throw new IllegalArgumentException("Select only one type parameter");
        }
        if(interval.isBlank() && dates.isEmpty()){
            throw new IllegalArgumentException("Select required time interval");
        }
        if(!interval.isBlank() && dates.size() == 2){
            throw new IllegalArgumentException("Select only interval parameter or start/end dates");
        }
    }

    private EventType checkEventType(Integer eventTypeNumber, String eventTypeName){
        EventType eventType = null;
        if(eventTypeName.isBlank()) {
            eventType = EventType.of(eventTypeNumber);
        } else if(eventTypeNumber == 0){
            eventType = EventType.get(eventTypeName);
        }
        return eventType;
    }

    private Interval getInterval(String interval){
        if(interval != null) {
            return Interval.get(interval);
        }
        return null;
    }

    private List<LocalDateTime> getDates(List<String> dates){
        if(dates != null) {
            return List.of(LocalDateTime.parse(dates.get(0)), LocalDateTime.parse(dates.get(1)));
        }
        return null;
    }
}
