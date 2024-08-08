package faang.school.analytics.util;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public final class TestDataFactory {

    public static final Long ID = 23L;
    public static final String INTERVAL = "LAST_YEAR";
    public static final String EVENT_TYPE = "GOAL_COMPLETED";


    public static AnalyticsEvent createAnalyticsEvent(){
        return AnalyticsEvent.builder()
                .id(23L)
                .receiverId(34L)
                .actorId(45L)
                .eventType(EventType.GOAL_COMPLETED)
                .receivedAt(LocalDateTime.now())
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
}
