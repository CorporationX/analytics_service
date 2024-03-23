package faang.school.analytics.mapper;

import faang.school.analytics.dto.follower.FollowerEventDto;

import static org.junit.jupiter.api.Assertions.*;

import faang.school.analytics.dto.MentorshipRequestedEvent;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventMapperTest {
    @Spy
    private AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);
    private AnalyticsEvent event;
    private AnalyticsEvent recommendationAnaliticsEvent;
    private FollowerEventDto eventDto;

    private RecommendationEvent recommendationEvent;
    LocalDateTime fixedTime = LocalDateTime.of(2024, 2, 22, 20, 6, 30);

    @BeforeEach
    void setUp() {
        event = AnalyticsEvent.builder()
                .receiverId(1L)
                .actorId(2L)
                .receivedAt(fixedTime)
                .build();

        recommendationAnaliticsEvent = AnalyticsEvent.builder()
                .id(2L)
                .receiverId(3L)
                .actorId(1L)
                .receivedAt(fixedTime)
                .build();

        eventDto = FollowerEventDto.builder()
                .followeeId(1L)
                .followerId(2L)
                .subscriptionTime(fixedTime)
                .build();

        recommendationEvent = RecommendationEvent.builder()
                .authorId(1L)
                .recommendationId(2L)
                .receiverId(3L)
                .createdAt(fixedTime)
                .build();
    }

    @Test
    public void testMapper() {
        assertEquals(event, analyticsEventMapper.toEntity(eventDto));
    }
  
    @Test
    public void testMapperMentorshipRequestedEvent() {
        MentorshipRequestedEvent mentorshipRequestedEvent = new MentorshipRequestedEvent(2L, 1L, fixedTime);
        assertEquals(event.getReceiverId(), analyticsEventMapper.mentorshipRequestedEventToAnalyticsEvent(mentorshipRequestedEvent).getReceiverId());
        assertEquals(event.getActorId(), analyticsEventMapper.mentorshipRequestedEventToAnalyticsEvent(mentorshipRequestedEvent).getActorId());
    }

    @Test
    void testToEntity() {
        assertEquals(event, analyticsEventMapper.toEntity(eventDto));
    }

}

