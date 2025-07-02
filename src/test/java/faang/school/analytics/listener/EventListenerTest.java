package faang.school.analytics.listener;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventListenerTest {

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Spy
    private AnalyticsEventMapperImpl mapper;

    @InjectMocks
    private EventListener eventListener;

    @Test
    void saveEventTest() {
        AnalyticsEventDto event = AnalyticsEventDto.builder()
                .receiverId(1L)
                .actorId(2L)
                .receivedAt(LocalDateTime.now())
                .build();

        EventType type = EventType.POST_LIKE;

        eventListener.saveEvent(event, type);

        verify(analyticsEventService, times(1)).saveEvent(any(AnalyticsEvent.class));
    }
}