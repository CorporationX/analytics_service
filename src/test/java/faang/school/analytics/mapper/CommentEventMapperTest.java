package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CommentEvent;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentEventMapperTest {
    private final CommentEventMapper mapper = Mappers.getMapper(CommentEventMapper.class);

    @Test
    void shouldMapCommentEventToAnalyticsEventDtoCorrectly() {
        // given
        var commentEvent = CommentEvent.builder()
                .commentId(12L)
                .receiverId(23L)
                .authorId(34L)
                .createdAt(LocalDateTime.MIN)
                .build();
        var expectedResult = AnalyticsEventDto.builder()
                .receiverId(23L)
                .actorId(34L)
                .eventType("post_comment")
                .receivedAt(LocalDateTime.MIN)
                .build();

        // when
        var actualResult = mapper.toAnalyticsEventDto(commentEvent);

        // then
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).usingRecursiveComparison()
                .ignoringFields("eventType")
                .isEqualTo(expectedResult);
        assertThat(actualResult.eventType()).isEqualToIgnoringCase(expectedResult.eventType());
    }
}