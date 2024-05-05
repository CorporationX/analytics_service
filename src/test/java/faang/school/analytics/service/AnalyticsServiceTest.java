package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    private AnalyticsEvent analyticsEvent;
    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @BeforeEach
    public void setup() {
        analyticsEvent = new AnalyticsEvent();
        analyticsEvent.setActorId(2);
        analyticsEvent.setReceiverId(3);
        analyticsEvent.setReceivedAt(LocalDateTime.now());
        analyticsEvent.setEventType(EventType.RECOMMENDATION_RECEIVED);
    }

    @Test
    public void testSuccessSavedAnalyticEvent() {
        Mockito.when(analyticsEventRepository.save(analyticsEvent)).thenReturn(analyticsEvent);

        analyticsEventService.saveEvent(analyticsEvent);

        Mockito.verify(analyticsEventRepository, Mockito.times(1)).save(analyticsEvent);
    }

    @Test
    public void testSaveEvent() {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent(1, 2, 1
                , EventType.PROFILE_APPEARED_IN_SEARCH, LocalDateTime.now());
        AnalyticsEvent.builder()
                .receiverId(2)
                .receivedAt(LocalDateTime.now())
                .actorId(1).eventType(EventType.PROFILE_APPEARED_IN_SEARCH).build();

        assertDoesNotThrow(() -> analyticsEventService.saveEvent(analyticsEvent));

        verify(analyticsEventRepository).save(analyticsEvent);
    }

    @Test
    public void test_saveEventAnalytics_Successful(){
        AnalyticsEvent analyticsEvent = new AnalyticsEvent(1, 1, 5, EventType.PROFILE_VIEW, LocalDateTime.now());
        Assertions.assertDoesNotThrow(() -> analyticsEventService.saveEvent(analyticsEvent));
        verify(analyticsEventRepository).save(analyticsEvent);
    }
}

