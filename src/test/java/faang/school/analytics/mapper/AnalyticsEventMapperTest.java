package faang.school.analytics.mapper;

import faang.school.analytics.model.FollowerEvent;
import faang.school.analytics.model.entity.AnalyticsEvent;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AnalyticsEventMapperTest {
    private final AnalyticsEventMapper mapper = Mappers.getMapper(AnalyticsEventMapper.class);

    @Test
    public void testFromFollowerEventToEntity() {
        FollowerEvent followerEvent = new FollowerEvent();
        followerEvent.setFollowerId(1L);
        followerEvent.setFollowedUserId(2L);
        followerEvent.setSubscriptionTime(LocalDateTime.of(2024, 10, 10, 12, 34, 56));

        AnalyticsEvent analyticsEvent = mapper.fromFollowerEventToEntity(followerEvent);

        assertNotNull(analyticsEvent);
        assertEquals(followerEvent.getFollowerId(), analyticsEvent.getActorId());
        assertEquals(followerEvent.getFollowedUserId(), analyticsEvent.getReceiverId());
        assertEquals(followerEvent.getSubscriptionTime(), analyticsEvent.getReceivedAt());
    }
}