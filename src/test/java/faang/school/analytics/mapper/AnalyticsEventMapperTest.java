package faang.school.analytics.mapper;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.dto.PostViewEvent;
import faang.school.analytics.model.dto.ProfileViewEvent;
import faang.school.analytics.model.mapper.AnalyticsEventMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AnalyticsEventMapperTest {

    private final AnalyticsEventMapper mapper = Mappers.getMapper(AnalyticsEventMapper.class);

    @Test
    void toDtoSuccessTest() {

        AnalyticsEvent event = new AnalyticsEvent();
        event.setId(1L);
        event.setReceiverId(2L);
        event.setActorId(3L);
        event.setEventType(EventType.POST_COMMENT);
        event.setReceivedAt(LocalDateTime.of(2023, 12, 10, 14, 30, 0));

        AnalyticsEventDto dto = mapper.toDto(event);

        assertNotNull(dto);
        assertEquals(1L, dto.id());
        assertEquals(2L, dto.receiverId());
        assertEquals(3L, dto.actorId());
        assertEquals("POST_COMMENT", dto.eventType());
        assertEquals(LocalDateTime.of(2023, 12, 10, 14, 30, 0), dto.receivedAt());
    }

    @Test
    void toEntitySuccessTest() {
        AnalyticsEventDto dto = new AnalyticsEventDto(
                1L,
                2L,
                3L,
                "POST_COMMENT",
                LocalDateTime.of(2023, 12, 10, 14, 30, 0)
        );
        AnalyticsEvent event = mapper.toEntity(dto);

        assertNotNull(event);
        assertEquals(1L, event.getId());
        assertEquals(2L, event.getReceiverId());
        assertEquals(3L, event.getActorId());
        assertEquals(EventType.POST_COMMENT, event.getEventType());
        assertEquals(LocalDateTime.of(2023, 12, 10, 14, 30, 0), event.getReceivedAt());
    }

    @Test
    void testNullValuesSuccessTest() {
        AnalyticsEventDto dto = mapper.toDto(null);
        assertNull(dto);

        AnalyticsEvent event = mapper.toEntity(null);
        assertNull(event);
    }


    @Test
    void testToEntityFromProfileViewEvent() {
        LocalDateTime now = LocalDateTime.now();
        ProfileViewEvent profileViewEvent = new ProfileViewEvent(202L, 101L, now);

        AnalyticsEvent analyticsEvent = mapper.toEntityFromProfileViewEvent(profileViewEvent);
        assertNotNull(analyticsEvent, "AnalyticsEvent should not be null after mapping.");

        assertEquals(EventType.PROFILE_VIEW, analyticsEvent.getEventType(),
                "eventType should be mapped as constant: PROFILE_VIEW.");
        assertEquals(now, analyticsEvent.getReceivedAt(),
                "receivedAt should match the createdTime of the source.");
        assertEquals(101L, analyticsEvent.getReceiverId(),
                "receiverId should match idUser from the source.");
        assertEquals(202L, analyticsEvent.getActorId(),
                "actorId should match idRequester from the source.");
    }

    @Test
    void testToEntityFromProfileViewEvent_NullSource() {
        AnalyticsEvent analyticsEvent = mapper.toEntityFromProfileViewEvent(null);
        assertNull(analyticsEvent, "If source is null, result should be null (MapStruct default).");
    }

    @Test
    void testToEntityFromPostViewEvent() {
        LocalDateTime now = LocalDateTime.now();
        PostViewEvent postViewEvent = new PostViewEvent(10, 11, 12, now);

        AnalyticsEvent analyticsEvent = mapper.toEntityFromPostViewEvent(postViewEvent);
        assertNotNull(analyticsEvent, "AnalyticsEvent should not be null after mapping.");

        assertEquals(EventType.POST_VIEW, analyticsEvent.getEventType(),
                "eventType should be mapped as constant: POST_VIEW.");
        assertEquals(now, analyticsEvent.getReceivedAt(),
                "receivedAt should match the timestamp of the source.");
        assertEquals(11, analyticsEvent.getReceiverId(),
                "receiverId should match authorId from the source.");
        assertEquals(12, analyticsEvent.getActorId(),
                "actorId should match userId from the source.");
    }

    @Test
    void testToEntityFromPPostViewEvent_NullSource() {
        AnalyticsEvent analyticsEvent = mapper.toEntityFromPostViewEvent(null);
        assertNull(analyticsEvent, "If source is null, result should be null (MapStruct default).");
    }
}