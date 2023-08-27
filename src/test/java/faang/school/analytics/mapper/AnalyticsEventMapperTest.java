package faang.school.analytics.mapper;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
class AnalyticsEventMapperTest {
    private final AnalyticsEventMapper analyticsEventMapper = new AnalyticsEventMapperImpl();

    @Test
    void testToEntity_MappingSuccess() {
        LocalDateTime createdAt = LocalDateTime.now();
        CommentEventDto commentEventDto = CommentEventDto.builder()
                .postId(1L)
                .authorId(2L)
                .commentId(3L)
                .createdAt(createdAt)
                .build();

        AnalyticsEvent result = analyticsEventMapper.toEntity(commentEventDto);

        assertEquals(commentEventDto.getAuthorId(), result.getActorId());
        assertEquals(commentEventDto.getCreatedAt(), result.getReceivedAt());
    }

    @Test
    void testToDto_MappingSuccess() {
        LocalDateTime receivedAt = LocalDateTime.now();
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .actorId(2L)
                .receivedAt(receivedAt)
                .build();

        CommentEventDto result = analyticsEventMapper.toDto(analyticsEvent);

        assertEquals(analyticsEvent.getActorId(), result.getAuthorId());
        assertEquals(analyticsEvent.getReceivedAt(), result.getCreatedAt());
    }
}