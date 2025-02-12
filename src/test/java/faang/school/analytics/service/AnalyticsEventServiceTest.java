package faang.school.analytics.service;

import faang.school.analytics.exception.DuplicatedEventException;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    void addEvent() {
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(2L)
                .actorId(3L)
                .eventType(EventType.POST_LIKE)
                .build();

        when(analyticsEventRepository
                .findByActorIdAndReceiverIdAndEventType(3L, 2L, EventType.POST_LIKE))
                .thenReturn(null);

        when(analyticsEventRepository.save(analyticsEvent)).thenReturn(analyticsEvent);
        Assertions.assertDoesNotThrow(() -> analyticsEventService.addEvent(analyticsEvent));

        verify(analyticsEventRepository).save(analyticsEvent);
        verify(analyticsEventRepository).findByActorIdAndReceiverIdAndEventType(3L, 2L,
                EventType.POST_LIKE);
    }

    @Test
    void addEventIfExist() {
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(2L)
                .actorId(3L)
                .eventType(EventType.POST_LIKE)
                .build();

        when(analyticsEventRepository
                .findByActorIdAndReceiverIdAndEventType(3L, 2L, EventType.POST_LIKE))
                .thenReturn(analyticsEvent);

        Assertions.assertThrows(DuplicatedEventException.class, () -> analyticsEventService.addEvent(analyticsEvent));

        verify(analyticsEventRepository).findByActorIdAndReceiverIdAndEventType(3L, 2L,
                EventType.POST_LIKE);
    }
}