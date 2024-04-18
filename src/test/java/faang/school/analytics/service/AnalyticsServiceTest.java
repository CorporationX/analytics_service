package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @InjectMocks
    private AnalyticsService analyticsService;
    private AnalyticsEvent analyticsEvent;

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

        analyticsService.saveEvent(analyticsEvent);

        Mockito.verify(analyticsEventRepository, Mockito.times(1)).save(analyticsEvent);
    }
}
