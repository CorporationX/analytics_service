package faang.school.analytics.service;

import static org.mockito.Mockito.*;

import faang.school.analytics.dto.MentorshipRequestedEvent;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.dto.follower.FollowerEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    @InjectMocks
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    private AnalyticsEvent analyticsEvent;
    private RecommendationEvent recommendationEvent;
    @Captor
    private ArgumentCaptor<AnalyticsEvent> captor;
    LocalDateTime fixedTime = LocalDateTime.of(2024, 2, 22, 20, 6, 30);
    private FollowerEventDto eventDto;
  
    @BeforeEach
    void setUp() {
        LocalDateTime fixedTime = LocalDateTime.of(2024, 2, 22, 20, 6, 30);
        analyticsEvent = AnalyticsEvent.builder()
                .id(2L)
                .actorId(1L)
                .receiverId(3L)
                .receivedAt(fixedTime)
                .eventType(EventType.RECOMMENDATION_RECEIVED)
                .build();
        recommendationEvent = RecommendationEvent.builder()
                .authorId(1L)
                .recommendationId(2L)
                .receiverId(3L)
                .createdAt(fixedTime)
                .build();
        eventDto = FollowerEventDto.builder()
                .followeeId(1L)
                .followerId(2L)
                .subscriptionTime(LocalDateTime.now())
                .build();
    }

    @Test
    public void testAnalyticsEventIsSaved() {
        when(analyticsEventMapper.recomendationEventToAnalyticsEvent(recommendationEvent)).thenReturn(analyticsEvent);
        analyticsEventService.saveRecommendationEvent(recommendationEvent);
        verify(analyticsEventRepository, times(1)).save(captor.capture());
    }

    @Test
    public void testMentorshipRequestedIsSaved() {
        MentorshipRequestedEvent mentorshipRequestedEvent = new MentorshipRequestedEvent(1L, 3L, fixedTime);

        when(analyticsEventMapper.mentorshipRequestedEventToAnalyticsEvent(mentorshipRequestedEvent)).thenReturn(analyticsEvent);
        analyticsEventService.saveMentorshipRequestedEvent(mentorshipRequestedEvent);
        verify(analyticsEventRepository, times(1)).save(captor.capture());
    }

}
