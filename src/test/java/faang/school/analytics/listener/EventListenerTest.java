package faang.school.analytics.listener;

import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, Object> map = new HashMap<>();
        map.put("authorId", 1L);
        map.put("receiverId", 2L);
        map.put("receivedAt", new ArrayList<>(List.of(2025, 6, 24, 17, 1, 1)));

        ConsumerRecord<String, Object> event =
                new ConsumerRecord<>("like", 0, 123L, "key", map);
        EventType type = EventType.POST_LIKE;

        eventListener.saveEvent(event, type);

        verify(analyticsEventService, times(1)).saveEvent(any(AnalyticsEvent.class));
    }
}