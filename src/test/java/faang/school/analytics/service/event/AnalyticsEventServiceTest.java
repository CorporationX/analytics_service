package faang.school.analytics.service.event;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    public void setUp() {
        analyticsEvent = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(2L)
                .actorId(3L)
                .eventType(EventType.RECOMMENDATION_RECEIVED)
                .receivedAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void testSaveRecommendationEvent_Success() {
        when(analyticsEventRepository.save(analyticsEvent)).thenReturn(analyticsEvent);

        analyticsEventService.saveRecommendationEvent(analyticsEvent);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    public void testSaveRecommendationEvent_NullEvent() {
        assertThrows(IllegalArgumentException.class, () -> analyticsEventService.saveRecommendationEvent(null));
        verify(analyticsEventRepository, never()).save(any());
    }

    @Test
    public void testSaveRecommendationEvent_MultipleCalls() {
        AnalyticsEvent anotherEvent = AnalyticsEvent.builder()
                .id(2L)
                .receiverId(4L)
                .actorId(5L)
                .eventType(EventType.RECOMMENDATION_RECEIVED)
                .receivedAt(LocalDateTime.now())
                .build();

        when(analyticsEventRepository.save(analyticsEvent)).thenReturn(analyticsEvent);
        when(analyticsEventRepository.save(anotherEvent)).thenReturn(anotherEvent);

        analyticsEventService.saveRecommendationEvent(analyticsEvent);
        analyticsEventService.saveRecommendationEvent(anotherEvent);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
        verify(analyticsEventRepository, times(1)).save(anotherEvent);
        verify(analyticsEventRepository, times(2)).save(any(AnalyticsEvent.class));
    }
}