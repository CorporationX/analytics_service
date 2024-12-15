package faang.school.analytics.mapper;

import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.event.NewCommentEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AnalyticsEventMapperTest {

    private AnalyticsEventMapper mapper;

    private NewCommentEvent newCommentEvent;
    private GoalCompletedEvent goalCompletedEvent;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(AnalyticsEventMapper.class);
    }

    @Test
    @DisplayName("Mapping new comment event Success")
    void testNewCommentEventToEntity_Success() {
        newCommentEvent = new NewCommentEvent(1L, 1L, 1L, LocalDateTime.of(2024, 12, 13, 0, 0));

        AnalyticsEvent result = mapper.newCommentEventToEntity(newCommentEvent);

        assertEquals(1L, result.getReceiverId());
        assertEquals(1L, result.getActorId());
        assertEquals(LocalDateTime.of(2024, 12, 13, 0, 0), result.getReceivedAt());
        assertEquals(EventType.POST_COMMENT, result.getEventType());
    }

    @Test
    @DisplayName("Mapping new comment event: null event")
    void testNewCommentEventToEntity_NullEvent() {
        newCommentEvent = null;

        AnalyticsEvent result = mapper.newCommentEventToEntity(newCommentEvent);

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