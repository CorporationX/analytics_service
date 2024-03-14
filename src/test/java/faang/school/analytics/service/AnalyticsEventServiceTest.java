package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.MentorshipRequestedEvent;
import faang.school.analytics.dto.PremiumBoughtEvent;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.dto.follower.FollowerEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    @InjectMocks
    private AnalyticsEventService analyticsEventService;
    @Spy
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

    @Test
    public void testSavePremiumBoughtEventSuccessful() {
        PremiumBoughtEvent premiumBoughtEvent = PremiumBoughtEvent.builder()
                .userId(1L)
                .price(10)
                .subscriptionDurationInDays(30)
                .purchaseDateTime(fixedTime)
                .build();
        analyticsEventService.savePremiumBoughtEvent(premiumBoughtEvent);
        verify(analyticsEventRepository).save(any(AnalyticsEvent.class));
    }

    @Test
    public void testSaveEventSuccessful() {
        analyticsEventService.saveEvent(analyticsEvent);
        verify(analyticsEventRepository).save(analyticsEvent);
    }

    @Test
    public void testGetAnalyticsSuccessfulInterval() {
        Interval interval = new Interval(LocalDateTime.now(), 10);
        EventType succesEventType = EventType.FOLLOWER;
        EventType failureEventType = EventType.POST_LIKE;
        long succesReceiverId = 3;
        long failureReceiverId = 6;
        int successIntervalDays = 5;
        int failureIntervalDays = 15;

        AnalyticsEvent successAnalytics = getAnalyticsEvent(
                succesReceiverId,
                succesEventType,
                successIntervalDays);
        AnalyticsEvent failureAnalytics = getAnalyticsEvent(
                failureReceiverId,
                failureEventType,
                failureIntervalDays);

        when(analyticsEventRepository.findByReceiverIdAndEventType(succesReceiverId, succesEventType))
                .thenReturn(Stream.of(successAnalytics, failureAnalytics));

        List<AnalyticsEventDto> results = analyticsEventService.getAnalytics(
                succesReceiverId, succesEventType, interval, fixedTime, fixedTime);

        assertEquals(analyticsEventMapper.toAnalyticsDto(successAnalytics), results.get(0));
    }

    private AnalyticsEvent getAnalyticsEvent(long receiverId,
                                             EventType eventType,
                                             int intervalDays) {
        return AnalyticsEvent.builder()
                .id(receiverId)
                .receiverId(receiverId)
                .actorId(receiverId)
                .eventType(eventType)
                .receivedAt(LocalDateTime.now().minusDays(intervalDays))
                .build();
    }
}
