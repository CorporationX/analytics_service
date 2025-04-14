package faang.school.analytics.listener;

import faang.school.analytics.dto.event.RecommendationEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class RecommendationEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private RecommendationEvent recommendationEvent;

    @InjectMocks
    private RecommendationEventListener recommendationEventListener;

    @Test
    @DisplayName("Проверка на успешное выполенение метода handleEvent")
    public void givenValidData_whenHandleEvent_thenSuccess() {
        recommendationEvent.setAuthorId(1L);
        recommendationEvent.setReceiverId(2L);

        recommendationEventListener.handleEvent(recommendationEvent);
    }
}