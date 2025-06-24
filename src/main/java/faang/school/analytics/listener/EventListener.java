package faang.school.analytics.listener;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class EventListener {

    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    public void saveEvent(ConsumerRecord<String, Object> event, EventType eventType) {
        Map<String, Object> eventData = (Map<String, Object>) event.value();
        AnalyticsEventDto eventDto = convertToEventLikeDto(eventData);

        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(eventDto);
        analyticsEvent.setEventType(eventType);

        analyticsEventService.saveEvent(analyticsEvent);
    }

    private AnalyticsEventDto convertToEventLikeDto(Map<String, Object> event) {
        return AnalyticsEventDto.builder()
                .actorId(Long.parseLong(event.get("authorId").toString()))
                .receiverId(Long.parseLong(event.get("receiverId").toString()))
                .receivedAt(parseDateTime(event.get("receivedAt")))
                .build();
    }

    private LocalDateTime parseDateTime(Object dateTime) {
        if (dateTime == null) {
            return LocalDateTime.now();
        }

        if (dateTime instanceof ArrayList) {
            ArrayList<Object> dateArray = (ArrayList<Object>) dateTime;

            int year = (int) dateArray.get(0);
            int month = (int) dateArray.get(1);
            int day = (int) dateArray.get(2);
            int hour = (int) dateArray.get(3);
            int minute = (int) dateArray.get(4);
            int second = (int) dateArray.get(5);

            return LocalDateTime.of(year, month, day, hour, minute, second);
        }

        log.warn("Unsupported date format");
        return LocalDateTime.now();
    }
}
