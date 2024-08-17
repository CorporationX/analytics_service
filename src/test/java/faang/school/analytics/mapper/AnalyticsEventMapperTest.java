package faang.school.analytics.mapper;

import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventMapperTest {

    private AnalyticsEventMapper mapper;
    private AnalyticsEvent analyticsEvent;
    private FollowerEvent followerEvent;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(AnalyticsEventMapper.class);
        analyticsEvent = AnalyticsEvent.builder().id(0L).actorId(1L).receiverId(2L).eventType(EventType.FOLLOWER)
                .receivedAt(LocalDateTime.of(2024,8, 17, 0, 0)).build();
        followerEvent = FollowerEvent.builder().followerId(1L).followeeId(2L).
                followTime(LocalDateTime.of(2024,8, 17, 0, 0)).build();
    }

    @Test
    public void toEntityTest() {
        AnalyticsEvent actualAnalyticsEvent = mapper.toEntity(followerEvent);
        Assert.assertEquals(analyticsEvent, actualAnalyticsEvent);
    }
}
