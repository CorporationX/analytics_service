package faang.school.analytics.mapper;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnalyticsEventMapperTest {
    private AnalyticsEventMapper analyticsEventMapper = new AnalyticsEventMapperImpl();

    @Test
    void testToEntity() {
        // Arrange
        CommentEventDto commentEvent = CommentEventDto.builder()
                .authorId(1L)
                .receiverId(10L)
                .createdAt(LocalDateTime.now()).build();

        // Act
        AnalyticsEvent entity = analyticsEventMapper.toEntity(commentEvent);

        // Assert
        assertAll(
                () -> assertEquals(entity.getActorId(), commentEvent.getAuthorId()),
                () -> assertEquals(entity.getReceiverId(), commentEvent.getReceiverId()),
                () -> assertEquals(entity.getReceivedAt(), commentEvent.getCreatedAt()),
                () -> assertEquals(entity.getEventType(), EventType.POST_COMMENT)
        );
    }
}