package faang.school.analytics.mapper;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.dto.AnalyticsEventDto;
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
        assertEquals("2023-12-10 14:30:00", dto.receivedAt());
    }

    @Test
    void toEntitySuccessTest() {
        AnalyticsEventDto dto = new AnalyticsEventDto(
                1L,
                2L,
                3L,
                "POST_COMMENT",
                "2023-12-10 14:30:00"
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
}
