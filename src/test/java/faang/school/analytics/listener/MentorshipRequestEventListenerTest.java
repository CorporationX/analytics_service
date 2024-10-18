package faang.school.analytics.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.dto.MentorshipRequestedEvent;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class MentorshipRequestEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventServiceImpl analyticsEventService;

    @Spy
    private AnalyticsEventMapper mapper;

    @InjectMocks
    private MentorshipRequestedEventListener mentorshipRequestedEventListener;

    @Test
    void testOnMessage_Success() throws Exception {
        Long requesterId = 1L;
        Long receiverId = 2L;

        MentorshipRequestedEvent mentorshipRequestEvent = new MentorshipRequestedEvent (requesterId, receiverId);

        String messageBody = "{\"requesterId\":" + requesterId + ", \"receiverId\":" + receiverId + "\"}";
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(messageBody.getBytes());

        when(objectMapper.readValue(any(byte[].class), eq(MentorshipRequestedEvent.class))).thenReturn(mentorshipRequestEvent);

        mentorshipRequestedEventListener.onMessage(message, null);

        ArgumentCaptor<AnalyticsEvent> eventCaptor = ArgumentCaptor.forClass(AnalyticsEvent.class);
        verify(analyticsEventService, times(1)).saveEvent(eventCaptor.capture());

        AnalyticsEvent savedEvent = eventCaptor.getValue();
        assertEquals(requesterId, savedEvent.getReceiverId());
        assertEquals(receiverId, savedEvent.getActorId());
        assertEquals(EventType.MENTORSHIP_REQUEST, savedEvent.getEventType());
    }

    @Test
    void testOnMessage_throwException() throws Exception {
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn("invalid".getBytes());

        when(objectMapper.readValue(any(byte[].class), eq(MentorshipRequestedEvent
                .class))).thenThrow(new IOException());

        assertThrows(RuntimeException.class, () -> mentorshipRequestedEventListener.onMessage(message, null));
        verify(objectMapper, times(1)).readValue(message.getBody(), MentorshipRequestedEvent.class);
        verifyNoMoreInteractions(mapper, analyticsEventService);
    }
}
