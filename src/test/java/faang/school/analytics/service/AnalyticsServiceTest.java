package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @InjectMocks
    private AnalyticsEventService analyticsEventService;

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
}
