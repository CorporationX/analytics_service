package faang.school.analytics.service.impl;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceImplTest {

    @Mock
    AnalyticsEventRepository analyticsEventRepository;
    @InjectMocks
    AnalyticsEventServiceImpl analyticsEventService;

    @Test
    @DisplayName("Test Save Event")
    void testSaveEvent() {
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .eventType(EventType.PROFILE_VIEW)
                .actorId(1L)
                .receivedAt(LocalDateTime.now())
                .build();
        analyticsEventService.saveEvent(analyticsEvent);
        Mockito.verify(analyticsEventRepository, Mockito.times(1))
                .save(analyticsEvent);
    }
}