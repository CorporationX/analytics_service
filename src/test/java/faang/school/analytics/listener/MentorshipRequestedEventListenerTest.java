package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.analyticsevents.AnalyticsEventMapperImpl;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.event.MentorshipRequestedEvent;
import faang.school.analytics.service.impl.analyticsevent.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipRequestedEventListenerTest {
    @Mock
    private AnalyticsEventServiceImpl analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;

    @Mock
    private Message message;

    @InjectMocks
    private MentorshipRequestedEventListener mentorshipRequestedEventListener;

    private MentorshipRequestedEvent mentorshipRequestedEvent;

    @BeforeEach
    void setUp() {
        mentorshipRequestedEvent = MentorshipRequestedEvent.builder().build();
        var json = "{\"userId\":1, \"receiverID\":2, \"requestedAt\": 2024-06-01T12:00:00}";
        when(message.getBody()).thenReturn(json.getBytes());
    }

    @Test
    @DisplayName("Test On Message Mentorship requested listener - Success")
    void testOnMessageSuccess() throws IOException {
        var analyticsEvent =  AnalyticsEvent.builder()
                .eventType(EventType.MENTORSHIP_REQUEST)
                .build();

        when(objectMapper.readValue(any(byte[].class), eq(MentorshipRequestedEvent.class))).
                thenReturn(mentorshipRequestedEvent);
        mentorshipRequestedEventListener.onMessage(message, null);
        verify(analyticsEventMapper).toEntity(mentorshipRequestedEvent);
        verify(analyticsEventService).saveEvent(analyticsEvent);
    }

    @Test
    void onMessage_shouldThrowRuntimeException_whenDeserializationFails() throws IOException {
        when(objectMapper.readValue(any(byte[].class), eq(MentorshipRequestedEvent.class)))
                .thenThrow(new JsonProcessingException("Test exception") {
                });

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() ->
                mentorshipRequestedEventListener.onMessage(message, null));
        verify(analyticsEventService, never()).saveEvent(any());
    }
}