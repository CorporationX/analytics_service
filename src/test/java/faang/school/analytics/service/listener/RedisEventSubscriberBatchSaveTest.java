package faang.school.analytics.service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RedisEventSubscriberBatchSaveTest {
    private static final String JSON = "[{\"receiverId\":1,\"actorId\":2,\"receivedAt\":\"2024-10-12T14:20:19\"}]";
    private static final long RECEIVER_ID = 1L;
    private static final long ACTOR_ID = 2L;
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2000, 1, 1, 1, 1);

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private Message message;

    @InjectMocks
    private RedisEventSubscriberBatchSaveImpl subscriber;

    @Test
    @DisplayName("Test analytic events list is empty successful")
    void testAnalyticsEventsListIsEmpty() {
        assertThat(subscriber.analyticsEventsListIsEmpty()).isTrue();
    }

    @Test
    public void testSaveAllEvents_successfulSave() {
        List<AnalyticsEvent> events = new CopyOnWriteArrayList<>();
        events.add(new AnalyticsEvent());
        subscriber.getAnalyticsEvents().addAll(events);

        subscriber.saveAllEvents();

        assertTrue(subscriber.getAnalyticsEvents().isEmpty());
        verify(analyticsEventService, times(1)).saveAllEvents(anyList());
    }

    @Test
    public void testSaveAllEvents_exceptionDuringSave() {
        List<AnalyticsEvent> events = new CopyOnWriteArrayList<>();
        events.add(new AnalyticsEvent());
        subscriber.getAnalyticsEvents().addAll(events);
        doThrow(new RuntimeException("test exception")).when(analyticsEventService).saveAllEvents(anyList());

        subscriber.saveAllEvents();

        assertFalse(subscriber.getAnalyticsEvents().isEmpty()); // Данные должны быть возвращены в список
        verify(analyticsEventService, times(1)).saveAllEvents(anyList());
    }

    public static class RedisEventSubscriberBatchSaveImpl extends RedisEventSubscriberBatchSave<ProfileViewEventDto> {
        public RedisEventSubscriberBatchSaveImpl(ObjectMapper objectMapper, AnalyticsEventMapper analyticsEventMapper,
                                                 AnalyticsEventService analyticsEventService) {
            super(objectMapper, analyticsEventMapper, analyticsEventService);
        }

        @Override
        public void onMessage(Message message, byte[] pattern) {

        }
    }
}