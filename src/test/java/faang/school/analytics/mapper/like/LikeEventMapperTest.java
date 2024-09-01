package faang.school.analytics.mapper.like;

import faang.school.analytics.dto.event.like.LikeEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LikeEventMapperTest {

    private final LikeEventMapperImpl likeEventMapper = new LikeEventMapperImpl();

    private AnalyticsEvent expectedAnalyticsEvent;
    private LikeEvent likeEvent;

    @BeforeEach
    void setUp() {
        UUID uuid = UUID.randomUUID();

        expectedAnalyticsEvent = AnalyticsEvent.builder()
                .eventId(uuid)
                .receiverId(1)
                .actorId(1)
                .receivedAt(LocalDateTime.MIN)
                .build();

        likeEvent = LikeEvent.builder()
                .eventId(uuid)
                .authorId(1)
                .postId(1)
                .timeStamp(LocalDateTime.MIN)
                .build();

    }

    @Test
    public void testToAnalyticsEventEntity() {
        AnalyticsEvent result = likeEventMapper.toAnalyticsEventEntity(likeEvent);

        assertEquals(expectedAnalyticsEvent.getEventId(), result.getEventId());
        assertEquals(expectedAnalyticsEvent.getActorId(), result.getActorId());
        assertEquals(expectedAnalyticsEvent.getReceiverId(), result.getReceiverId());
        assertEquals(expectedAnalyticsEvent.getReceivedAt(), result.getReceivedAt());
    }
}