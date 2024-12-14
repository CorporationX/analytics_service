package faang.school.analytics.service.event;

import faang.school.analytics.dto.event.EventDto;
import faang.school.analytics.dto.event.Interval;
import faang.school.analytics.mapper.event.EventMapper;
import faang.school.analytics.model.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventParamService {
    private final AnalyticsEventService eventService;
    private final EventMapper eventMapper;

    public List<EventDto> getEventsDto(long receiverId,
                                       String eventTypeStr,
                                       String intervalStr,
                                       String fromStr,
                                       String toStr) {

        EventType eventType = getEventType(eventTypeStr);
        LocalDateTime from = getLocalDateTime(fromStr);
        LocalDateTime to = getLocalDateTime(toStr);
        Interval interval = getInterval(intervalStr);

        return eventService.getEventsEntity(receiverId, eventType, interval, from, to).stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    private EventType getEventType(String event) {
        if (event == null || event.isBlank()) {
            throw new IllegalArgumentException("Не валидный эвент");
        }
        try {
            int id = Integer.parseInt(event);
            return EventType.of(id);
        } catch (NumberFormatException e) {
            try {
                return EventType.valueOf(event.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Такого эвента не существует");
            }
        }
    }

    private Interval getInterval(String interval) {
        if (interval == null || interval.isBlank()) {
            return null;
        }
        try {
            int id = Integer.parseInt(interval);
            return Interval.values()[id];
        } catch (NumberFormatException e) {
            try {
                return Interval.valueOf(interval.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Такого интервала не существует");
            }
        }
    }

    private LocalDateTime getLocalDateTime(String time) {
        if (time == null || time.isBlank()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            return LocalDateTime.parse(time, formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Не правильно введенное время");
        }
    }
}
