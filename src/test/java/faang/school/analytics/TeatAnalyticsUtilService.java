package faang.school.analytics;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.services.utils.AnalyticsUtilService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeatAnalyticsUtilService {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @InjectMocks
    private AnalyticsUtilService analyticsUtilService;

    @Test
    public void testSaveEvent(){
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        analyticsUtilService.saveEvent(analyticsEvent);
        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    public void testGetAnalytics(){
        long receiverId = 1L;
        EventType eventType = EventType.ACHIEVEMENT_RECEIVED;
        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId,eventType)).thenReturn(Stream.of(new AnalyticsEvent(), new AnalyticsEvent()));
        Stream<AnalyticsEvent> analyticsEvents = analyticsUtilService.getAnalytics(receiverId, eventType);
        assertEquals(2, analyticsEvents.count());
    }

}
