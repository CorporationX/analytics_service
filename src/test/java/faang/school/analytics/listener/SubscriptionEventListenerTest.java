package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.event.SubscriptionEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionEventListenerTest {

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Message message;

    @InjectMocks
    private SubscriptionEventListener subscriptionEventListener;

    private SubscriptionEvent subscriptionEvent;
    private AnalyticsEvent analyticsEvent;
    private ArgumentCaptor<AnalyticsEvent> analyticsEventCaptor;
    private String json;
    private String invalidJson;

    @BeforeEach
    void setUp() {
        analyticsEvent = AnalyticsEvent.builder().id(100L).build();
        subscriptionEvent = SubscriptionEvent.builder()
                .followerId(1L)
                .followeeId(2L)
                .subscribedAt(null)
                .build();
        analyticsEventCaptor = ArgumentCaptor.forClass(AnalyticsEvent.class);
        json = "{\"followerId\":1,\"followeeId\":2,\"subscribedAt\":\"2024-12-10T12:00:00\"}";
        invalidJson = "{\"followerId\":\"invalid_id\",\"followeeId\":2,\"subscribedAt\":\"invalid_date\"}";
    }

    @Test
    void testDeserializeJsonToSubscriptionEvent_Success() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        SubscriptionEvent event = objectMapper.readValue(json, SubscriptionEvent.class);

        assertThat(event).isNotNull();
        assertThat(event.getFollowerId()).isEqualTo(1L);
        assertThat(event.getFolloweeId()).isEqualTo(2L);
        assertThat(event.getSubscribedAt()).isEqualTo(LocalDateTime.of(2024, 12, 10, 12, 0));
    }

    @Test
    void testDeserializeJsonToSubscriptionEvent_ThrowsExceptionWithInvalidData() {
        objectMapper = new ObjectMapper();

        assertThatThrownBy(() -> objectMapper.readValue(invalidJson, SubscriptionEvent.class))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Cannot deserialize");
    }

    @Test
    void testOnMessage_Success() throws Exception {
        when(message.getBody()).thenReturn(json.getBytes(StandardCharsets.UTF_8));
        when(objectMapper.readValue(json, SubscriptionEvent.class)).thenReturn(subscriptionEvent);
        when(analyticsEventMapper.toEntity(subscriptionEvent)).thenReturn(analyticsEvent);
        when(analyticsEventService.saveEvent(analyticsEvent)).thenReturn(analyticsEvent);

        subscriptionEventListener.onMessage(message, null);

        verify(objectMapper, times(1)).readValue(json, SubscriptionEvent.class);
        verify(analyticsEventMapper, times(1)).toEntity(subscriptionEvent);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEventCaptor.capture());

        AnalyticsEvent capturedEvent = analyticsEventCaptor.getValue();
        assertThat(capturedEvent.getId()).isEqualTo(analyticsEvent.getId());
    }

    @Test
    void testOnMessage_HandlesExceptionWithoutThrowing() throws Exception {
        when(message.getBody()).thenReturn(invalidJson.getBytes(StandardCharsets.UTF_8));
        doAnswer(invocation -> {
            throw new IOException("Invalid data format");
        }).when(objectMapper).readValue(invalidJson, SubscriptionEvent.class);

        assertDoesNotThrow(() -> subscriptionEventListener.onMessage(message, null));

        verify(objectMapper, times(1)).readValue(invalidJson, SubscriptionEvent.class);
        verify(analyticsEventMapper, never()).toEntity(any(SubscriptionEvent.class));
        verify(analyticsEventService, never()).saveEvent(any());
    }
}