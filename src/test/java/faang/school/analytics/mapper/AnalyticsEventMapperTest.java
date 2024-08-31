package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventMapperTest {

    private AnalyticsEventMapperImpl analyticsEventMapper;
    private AnalyticsEventDto analyticsEventDto;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    public void setUp() {
        analyticsEventMapper = new AnalyticsEventMapperImpl();
        analyticsEventDto = new AnalyticsEventDto();
        analyticsEvent = new AnalyticsEvent();
    }

    @Test
    public void testToDto() {
        Assertions.assertEquals(analyticsEventDto, analyticsEventMapper.toDto(analyticsEvent));
    }

    @Test
    public void testLikeEventToAnalyticsEvent() {
        LocalDateTime eventAt = LocalDateTime.now();
        LikeEvent likeEvent = LikeEvent.builder()
                .postId(1L)
                .authorId(2L)
                .userId(3L)
                .eventAt(eventAt)
                .build();

        // When
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(likeEvent);

        // Then
        assertNotNull(analyticsEvent);
        assertEquals(2L, analyticsEvent.getReceiverId());
        assertEquals(3L, analyticsEvent.getActorId());
        assertEquals(EventType.POST_LIKE, analyticsEvent.getEventType());
        assertEquals(eventAt, analyticsEvent.getReceivedAt());
    }

}
