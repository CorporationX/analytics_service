package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    private AnalyticsEvent event;

    @BeforeEach
    void setUp() {
        event = AnalyticsEvent.builder()
                .receiverId(1L)
                .actorId(1L)
                .eventType(EventType.PREMIUM_BOUGHT)
                .receivedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testSave() {
        AnalyticsEvent savedEvent = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(1L)
                .actorId(1L)
                .eventType(EventType.PREMIUM_BOUGHT)
                .receivedAt(event.getReceivedAt())
                .build();

        when(analyticsEventRepository.save(any(AnalyticsEvent.class))).thenReturn(savedEvent);

        AnalyticsEvent result = analyticsEventService.save(event);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEventType()).isEqualTo(EventType.PREMIUM_BOUGHT);
        verify(analyticsEventRepository).save(event);
    }
}


