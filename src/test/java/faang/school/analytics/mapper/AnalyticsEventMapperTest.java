package faang.school.analytics.mapper;

import faang.school.analytics.event.SubscriptionEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AnalyticsEventMapperTest {
    private AnalyticsEventMapper analyticsEventMapper;

    @BeforeEach
    void setUp() {
        analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);
    }

    @Test
    void testSubscriptionEventToEntityMapping_CorrectValues() {
        SubscriptionEvent subscriptionEvent = SubscriptionEvent.builder()
                .followerId(1L)
                .followeeId(2L)
                .subscribedAt(LocalDateTime.now())
                .build();
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(subscriptionEvent);
        EventType eventType = EventType.FOLLOWER;

        assertThat(analyticsEvent.getActorId()).isEqualTo(subscriptionEvent.getFollowerId());
        assertThat(analyticsEvent.getReceiverId()).isEqualTo(subscriptionEvent.getFolloweeId());
        assertThat(analyticsEvent.getReceivedAt()).isEqualTo(subscriptionEvent.getSubscribedAt());
        assertThat(analyticsEvent.getEventType()).isEqualTo(eventType);
    }
}