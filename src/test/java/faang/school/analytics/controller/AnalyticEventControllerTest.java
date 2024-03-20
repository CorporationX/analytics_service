package faang.school.analytics.controller;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticEventControllerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;
    @InjectMocks
    private AnalyticsEventController analyticsEventController;

    private final long analyticsEventId = 1L;
    private final AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
            .id(analyticsEventId)
            .build();


    @Test
    void testGet() {
        when(analyticsEventService.getById(analyticsEventId)).thenReturn(analyticsEvent);

        AnalyticsEvent analyticsEventByController = analyticsEventController.get(analyticsEventId);

        assertEquals(analyticsEvent, analyticsEventByController);
    }

    @Test
    void testDelete() {
        analyticsEventController.delete(analyticsEventId);

        verify(analyticsEventService, times(1)).deleteEvent(analyticsEventId);
    }
}