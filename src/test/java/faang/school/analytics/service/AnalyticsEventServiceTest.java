package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Test
    @DisplayName("Test saveEvent")
    void saveEventTest() {
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(1L)
                .actorId(2L)
                .eventType(EventType.POST_LIKE)
                .build();

        when(analyticsEventRepository.save(analyticsEvent))
                .thenReturn(analyticsEvent);

        analyticsEventService.saveEvent(analyticsEvent);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }
}
