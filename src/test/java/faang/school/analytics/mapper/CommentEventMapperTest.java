package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentEventMapperTest {
    private final CommentEventMapper mapper = Mappers.getMapper(CommentEventMapper.class);

    @Test
    void shouldMapCommentEventToAnalyticsEventDtoCorrectly() {
        // given
        var commentEvent = TestDataFactory.createCommentEvent();
        var expectedResult = TestDataFactory.createAnalyticsEventDtoWithPostCommentEventType();

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