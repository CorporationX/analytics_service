package faang.school.analytics;

import faang.school.analytics.event.LikeEvent;
import faang.school.analytics.event.MentorshipRequestedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class AnalyticsEventMapperTest {

    @Autowired
    private AnalyticsEventMapper mapper;

    @Test
    void toEntity_shouldMapAllFieldsCorrectly() {
        LikeEvent likeEvent = new LikeEvent(100L,
                20L,
                10L,
                LocalDateTime.of(2024, 1, 1, 10, 15, 30));

        AnalyticsEvent result = mapper.toLikeEntity(likeEvent);

        assertEquals(20L, result.getAuthorId());
        assertEquals(10L, result.getReceiverId());
        assertEquals(EventType.POST_LIKE, result.getEventType());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 15, 30), result.getReceivedAt());
    }

    @Test
    void toMentorshipEntity_shouldMapAllFieldsCorrectly() {

        MentorshipRequestedEvent mentorshipEvent = new MentorshipRequestedEvent(
                30L,
                15L,
                LocalDateTime.of(2024, 5, 10, 14, 0, 0)
        );

        AnalyticsEvent result = mapper.toMentorshipEntity(mentorshipEvent);

        assertEquals(30L, result.getAuthorId());
        assertEquals(15L, result.getReceiverId());
        assertEquals(EventType.MENTORSHIP_REQUESTED, result.getEventType());
        assertEquals(LocalDateTime.of(2024, 5, 10, 14, 0, 0),
                result.getReceivedAt());
    }
}
