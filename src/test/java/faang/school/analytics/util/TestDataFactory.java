package faang.school.analytics.util;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public final class TestDataFactory {

    public static final Long ID = 23L;
    public static final LocalDateTime START_DATE = LocalDateTime.of(2024, 1, 1, 12, 0);
    public static final LocalDateTime END_DATE  = LocalDateTime.of(2024, 8, 12, 12, 0);


    public static AnalyticsEvent createAnalyticsEvent(){
        return AnalyticsEvent.builder()
                .id(23L)
                .receiverId(34L)
                .actorId(45L)
                .eventType(EventType.GOAL_COMPLETED)
                .receivedAt(LocalDateTime.now().minusHours(8))
                .build();
    }

    public static AnalyticsEventDto createAnalyticsEventDto(){
        return AnalyticsEventDto.builder()
                .id(23L)
                .receiverId(34L)
                .actorId(45L)
                .eventType("goal_completed")
                .receivedAt(LocalDateTime.now())
                .build();
    }

    public static Interval createInterval(){
        return Interval.LAST_YEAR;
    }
    public static EventType createEventType(){
        return EventType.GOAL_COMPLETED;
    }
}
