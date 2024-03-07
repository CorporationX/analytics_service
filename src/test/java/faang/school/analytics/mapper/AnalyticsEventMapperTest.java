package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.dto.CommentEventDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnalyticsEventMapperTest {
    private AnalyticsEventMapper analyticsEventMapper = new AnalyticsEventMapperImpl();

    @Test
    void testToDto() {
        // Arrange
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .id(1L)
                .receivedAt(LocalDateTime.now())
                .receiverId(10L)
                .eventType(EventType.POST_VIEW)
                .actorId(15L).build();

        // Act
        AnalyticsEventDto dto = analyticsEventMapper.toDto(analyticsEvent);

        // Assert
        assertAll(
                () -> assertEquals(dto.getActorId(), analyticsEvent.getActorId()),
                () -> assertEquals(dto.getReceiverId(), analyticsEvent.getReceiverId()),
                () -> assertEquals(dto.getReceivedAt(), analyticsEvent.getReceivedAt()),
                () -> assertEquals(dto.getId(), analyticsEvent.getId()),
                () -> assertEquals(dto.getEventType(), analyticsEvent.getEventType())
        );
    }

    @Test
    void testToAnalyticsEvent() {
        GoalCompletedEvent goalCompletedEvent = GoalCompletedEvent.builder().build();
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder().eventType(EventType.GOAL_COMPLETED).build();
        assertEquals(analyticsEvent, analyticsEventMapper.toAnalyticsEvent(goalCompletedEvent));
    }

    @Test
    void testToEntity() {
        // Arrange
        CommentEventDto commentEvent = CommentEventDto.builder()
                .authorId(1L)
                .receiverId(10L)
                .createdAt(LocalDateTime.now()).build();

        // Act
        AnalyticsEvent entity = analyticsEventMapper.toAnalyticsEvent(commentEvent);

        // Assert
        assertAll(
                () -> assertEquals(entity.getActorId(), commentEvent.getAuthorId()),
                () -> assertEquals(entity.getReceiverId(), commentEvent.getReceiverId()),
                () -> assertEquals(entity.getReceivedAt(), commentEvent.getCreatedAt()),
                () -> assertEquals(entity.getEventType(), EventType.POST_COMMENT)
        );
    }
}