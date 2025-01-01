package faang.school.analytics.model.mapper;

import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.dto.FollowerEvent;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FollowerEventMapperTest {
    private final FollowerEventMapper mapper = Mappers.getMapper(FollowerEventMapper.class);

    @Test
    void toAnalyticsEventDtoSuccessTest() {
        LocalDateTime localDateTime = LocalDateTime.now();
        FollowerEvent followerEvent = new FollowerEvent(101L, 202L, localDateTime);
        AnalyticsEventDto analyticsEventDto = mapper.toAnalyticsEventDto(followerEvent);
        assertEquals(followerEvent.followerId(), analyticsEventDto.receiverId());
        assertEquals(followerEvent.followeeId(), analyticsEventDto.actorId());
        assertEquals(followerEvent.receivedAt(), analyticsEventDto.receivedAt());
        assertEquals("FOLLOWER", analyticsEventDto.eventType());
    }

    @Test
    void toFollowerEvent() {
        LocalDateTime localDateTime = LocalDateTime.now();
        AnalyticsEventDto analyticsEventDto = new AnalyticsEventDto(1L, 101L,
                202L,  "FOLLOWER", localDateTime);
        FollowerEvent followerEvent = mapper.toFollowerEvent(analyticsEventDto);

        assertEquals(analyticsEventDto.receiverId(), followerEvent.followerId());
        assertEquals(analyticsEventDto.actorId(), followerEvent.followeeId());
        assertEquals(analyticsEventDto.receivedAt(), followerEvent.receivedAt());
    }
}