package faang.school.analytics.mapper;

import faang.school.analytics.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class AnalyticsEventMapperTest {

    private final AnalyticsEventMapper mapper = Mappers.getMapper(AnalyticsEventMapper.class);
    @Test
    void testMapToEntity() {
        // given - precondition
        var expectedResult = TestDataFactory.createAnalyticsEventDto();

        // when - action
        var actualResult = mapper.toEntity(expectedResult);

        // then - verify the output
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getReceiverId()).isEqualTo(expectedResult.receiverId());
        assertThat(actualResult.getActorId()).isEqualTo(expectedResult.actorId());
        assertThat(actualResult.getEventType().name()).isEqualTo(expectedResult.eventType().toUpperCase());
        assertThat(actualResult.getReceivedAt().toLocalDate())
                .isEqualTo(expectedResult.receivedAt().toLocalDate());
    }

    @Test
    void testMapToDto() {
        // given - precondition
        var expectedResult = TestDataFactory.createAnalyticsEvent();

        // when - action
        var actualResult = mapper.toDto(expectedResult);

        // then - verify the output
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.receiverId()).isEqualTo(expectedResult.getReceiverId());
        assertThat(actualResult.actorId()).isEqualTo(expectedResult.getActorId());
        assertThat(actualResult.eventType().toUpperCase()).isEqualTo(expectedResult.getEventType().name());
        assertThat(actualResult.receivedAt().toLocalDate())
                .isEqualTo(expectedResult.getReceivedAt().toLocalDate());
    }
}