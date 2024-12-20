package faang.school.analytics.mapper;

import faang.school.analytics.event.MentorshipRequestEvent;
import faang.school.analytics.event.SubscriptionEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class AnalyticsEventMapperTest {
    private AnalyticsEventMapper analyticsEventMapper;

    @BeforeEach
    void setUp() {
        analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);
    }

    @Test
    void testSubscriptionEventToEntityMapping_CorrectValues() {
        SubscriptionEvent subscriptionEvent = new SubscriptionEvent(1L, 2L, LocalDateTime.now());

        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(subscriptionEvent);
        EventType eventType = EventType.FOLLOWER;

        assertThat(analyticsEvent.getActorId()).isEqualTo(subscriptionEvent.getFollowerId());
        assertThat(analyticsEvent.getReceiverId()).isEqualTo(subscriptionEvent.getFolloweeId());
        assertThat(analyticsEvent.getReceivedAt()).isEqualTo(subscriptionEvent.getSubscribedAt());
        assertThat(analyticsEvent.getEventType()).isEqualTo(eventType);
    }

    @Test
    void testMentorshipRequestEventToAnalyticsEvent() {
        MentorshipRequestEvent mentorshipRequestEvent = new MentorshipRequestEvent(1L, 2L, null);

        AnalyticsEvent result = analyticsEventMapper.toAnalyticsEventMentorshipRequest(mentorshipRequestEvent);

        assertNotNull(result);
        assertEquals(1L, result.getReceiverId());
        assertEquals(2L, result.getActorId());
        assertNull(result.getReceivedAt());
    }
}