package faang.school.analytics.mapper;

import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FollowerEventMapperTest {

    private FollowerEventMapper followerEventMapper = new FollowerEventMapperImpl();

    @Test
    void toModelTest() {
        LocalDateTime currentTime = LocalDateTime.now();

        FollowerEventDto dto = FollowerEventDto.builder()
                .followerId(1)
                .followeeId(2)
                .subscriptionTime(currentTime)
                .build();

        AnalyticsEvent model = AnalyticsEvent.builder()
                .receiverId(2)
                .actorId(1)
                .receivedAt(currentTime)
                .build();

        AnalyticsEvent result = followerEventMapper.toModel(dto);

        assertEquals(model, result);
    }

    @Test
    void toDtoTest() {

    }
}