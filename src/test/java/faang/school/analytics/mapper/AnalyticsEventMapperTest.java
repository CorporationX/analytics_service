package faang.school.analytics.mapper;

import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.event.CommentEvent;
import faang.school.analytics.event.MentorshipRequestEvent;
import faang.school.analytics.event.SubscriptionEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class AnalyticsEventMapperTest {
    private AnalyticsEventMapper analyticsEventMapper;

    private AnalyticsEventMapper mapper;

    private CommentEvent commentEvent;
    private GoalCompletedEvent goalCompletedEvent;

    @BeforeEach
    void setUp() {
        analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);
        mapper = Mappers.getMapper(AnalyticsEventMapper.class);
    }

    @Test
    @DisplayName("Mapping new comment event Success")
    void testNewCommentEventToEntity_Success() {
        commentEvent = new CommentEvent(1L, 1L, 1L, 1L, LocalDateTime.of(2024, 12, 13, 0, 0));

        AnalyticsEvent result = mapper.newCommentEventToEntity(commentEvent);

        assertEquals(1L, result.getReceiverId());
        assertEquals(1L, result.getActorId());
        assertEquals(LocalDateTime.of(2024, 12, 13, 0, 0), result.getReceivedAt());
        assertEquals(EventType.POST_COMMENT, result.getEventType());
    }

    @Test
    void testSubscriptionEventToEntityMapping_CorrectValues() {
        SubscriptionEvent subscriptionEvent = new SubscriptionEvent(1L, 2L, LocalDateTime.now(), null, null);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(subscriptionEvent);
        EventType eventType = EventType.FOLLOWER;

        assertThat(analyticsEvent.getActorId()).isEqualTo(subscriptionEvent.followerId());
        assertThat(analyticsEvent.getReceiverId()).isEqualTo(subscriptionEvent.followeeId());
        assertThat(analyticsEvent.getReceivedAt()).isEqualTo(subscriptionEvent.subscribedAt());
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

    @Test
    @DisplayName("Mapping new comment event: null event")
    void testNewCommentEventToEntity_NullEvent() {
        commentEvent = null;

        AnalyticsEvent result = mapper.newCommentEventToEntity(commentEvent);

        assertNull(result);
    }

    @Test
    @DisplayName("Mapping goal completed event Success")
    void testGoalCompletedEventToEntity_Success() {
        goalCompletedEvent = new GoalCompletedEvent(1L, 1L, LocalDateTime.of(2024, 12, 13, 0, 0));

        AnalyticsEvent result = mapper.goalCompletedEventToEntity(goalCompletedEvent);

        assertEquals(1L, result.getReceiverId());
        assertEquals(1L, result.getActorId());
        assertEquals(LocalDateTime.of(2024, 12, 13, 0, 0), result.getReceivedAt());
        assertEquals(EventType.GOAL_COMPLETED, result.getEventType());
    }

    @Test
    @DisplayName("Mapping goal completed event: null event")
    void testGoalCompletedEventToEntity_NullEvent() {
        goalCompletedEvent = null;

        AnalyticsEvent result = mapper.goalCompletedEventToEntity(goalCompletedEvent);

        assertNull(result);
    }
}