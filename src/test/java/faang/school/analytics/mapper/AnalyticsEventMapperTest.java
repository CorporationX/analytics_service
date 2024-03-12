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
    private AnalyticsEvent analyticsEvent;
    private AnalyticsEvent recommendationAnaliticsEvent;
    private FollowerEventDto eventDto;

    private RecommendationEvent recommendationEvent;
    LocalDateTime fixedTime = LocalDateTime.of(2024, 2, 22, 20, 6, 30);

    @BeforeEach
    void setUp() {
        analyticsEvent = AnalyticsEvent.builder()
                .actorId(1L)
                .receiverId(3L)
                .receivedAt(fixedTime)
                .build();
      
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
        assertEquals(analyticsEvent, analyticsEventMapper.recomendationEventToAnalyticsEvent(recommendationEvent));
    }
  
    @Test
    public void testMapperMentorshipRequestedEvent() {
        MentorshipRequestedEvent mentorshipRequestedEvent = new MentorshipRequestedEvent(1L, 3L, fixedTime);
        assertEquals(analyticsEvent.getReceiverId(), analyticsEventMapper.mentorshipRequestedEventToAnalyticsEvent(mentorshipRequestedEvent).getReceiverId());
        assertEquals(analyticsEvent.getActorId(), analyticsEventMapper.mentorshipRequestedEventToAnalyticsEvent(mentorshipRequestedEvent).getActorId());
    }

    @Test
    void testToEntity() {
        assertEquals(event, analyticsEventMapper.toEntity(eventDto));
    }

}

