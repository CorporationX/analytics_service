package faang.school.analytics;

import faang.school.analytics.event.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class AnalyticsEventMapperTest {
    private final AnalyticsEventMapper mapper = new AnalyticsEventMapper();

    @Test
    void toEntity_shouldMapAllFieldsCorrectly() {
        LikeEvent likeEvent = new LikeEvent(
                100L,
                10L,
                20L,
                LocalDateTime.of(2024, 1, 1, 10, 15, 30)
        );

        AnalyticsEvent result = mapper.toEntity(likeEvent);

        assertEquals(10L, result.getReceiverId());
        assertEquals(20L, result.getAuthorId());
        assertEquals(EventType.POST_LIKE, result.getEventType());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 15, 30), result.getReceivedAt());
    }
}
