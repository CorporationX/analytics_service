package faang.school.analytics.mapper;

import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.dto.comment.CommentEvent;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AnalyticsEventMapperTest {
    private AnalyticsEventMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(AnalyticsEventMapper.class);
    }

    @Test
    void postViewEventDtoToAnalyticsEvent() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 10, 11, 12, 13, 15);
        PostViewEventDto dto = new PostViewEventDto(1L, 2L, 3L, dateTime);
        AnalyticsEvent expected = new AnalyticsEvent(0L, 2L, 3L, EventType.POST_VIEW, dateTime);
        AnalyticsEvent event = mapper.postViewEventDtoToAnalyticsEvent(dto);

        assertThat(expected)
                .usingRecursiveComparison()
                .isEqualTo(event);
    }
    @Test
    public void testToAnalyticsEventFromCommentEvent() {
        LocalDateTime timestamp = LocalDateTime.now();
        CommentEvent commentEvent = new CommentEvent(1L, 2L, 3L, timestamp);

        AnalyticsEvent analyticsEvent = mapper.toAnalyticsEvent(commentEvent);

        assertNotNull(analyticsEvent);
        assertEquals(commentEvent.getAuthorId(), analyticsEvent.getActorId());
        assertEquals(commentEvent.getCommentId(), analyticsEvent.getReceiverId());
        assertEquals(EventType.POST_COMMENT, analyticsEvent.getEventType());
        assertEquals(commentEvent.getTimestamp(), analyticsEvent.getReceivedAt());
    }
    @Test
    public void testToAnalyticsEventFromProfileViewEventDto() {
        LocalDateTime receivedAt = LocalDateTime.now();
        ProfileViewEventDto profileViewEventDto = new ProfileViewEventDto(1L, 2L, receivedAt);

        AnalyticsEvent analyticsEvent = mapper.toAnalyticsEvent(profileViewEventDto);

        assertNotNull(analyticsEvent);
        assertEquals(profileViewEventDto.getReceiverId(), analyticsEvent.getReceiverId());
        assertEquals(profileViewEventDto.getActorId(), analyticsEvent.getActorId());
        assertEquals(EventType.PROFILE_VIEW, analyticsEvent.getEventType());
        assertEquals(profileViewEventDto.getReceivedAt(), analyticsEvent.getReceivedAt());
    }
    @Test
    public void testToAnalyticsEventsFromProfileViewEventDtoList() {
        LocalDateTime receivedAt = LocalDateTime.now();
        ProfileViewEventDto dto1 = new ProfileViewEventDto(1L, 2L, receivedAt);
        ProfileViewEventDto dto2 = new ProfileViewEventDto(3L, 4L, receivedAt);

        List<ProfileViewEventDto> dtoList = Arrays.asList(dto1, dto2);

        List<AnalyticsEvent> analyticsEvents = mapper.toAnalyticsEvents(dtoList);

        assertNotNull(analyticsEvents);
        assertEquals(2, analyticsEvents.size());

        AnalyticsEvent event1 = analyticsEvents.get(0);
        assertEquals(dto1.getReceiverId(), event1.getReceiverId());
        assertEquals(dto1.getActorId(), event1.getActorId());
        assertEquals(EventType.PROFILE_VIEW, event1.getEventType());
        assertEquals(dto1.getReceivedAt(), event1.getReceivedAt());

        AnalyticsEvent event2 = analyticsEvents.get(1);
        assertEquals(dto2.getReceiverId(), event2.getReceiverId());
        assertEquals(dto2.getActorId(), event2.getActorId());
        assertEquals(EventType.PROFILE_VIEW, event2.getEventType());
        assertEquals(dto2.getReceivedAt(), event2.getReceivedAt());
    }
}