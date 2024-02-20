package faang.school.analytics.mapper;

import faang.school.analytics.dto.follower.FollowerEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventMapperTest {
    @Spy
    private AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);
    private AnalyticsEvent event;
    private FollowerEventDto eventDto;

    @BeforeEach
    void setUp() {
        LocalDateTime fixedTime = LocalDateTime.of(
                2024, Month.FEBRUARY, 20, 12, 0, 0);

        event = AnalyticsEvent.builder()
                .receiverId(1L)
                .actorId(2L)
                .receivedAt(fixedTime)
                .build();

        eventDto = FollowerEventDto.builder()
                .followeeId(1L)
                .followerId(2L)
                .subscriptionTime(fixedTime)
                .build();
    }

    @Test
    void testToEntity() {
        assertEquals(event, analyticsEventMapper.toEntity(eventDto));

    }
}