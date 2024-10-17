package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEventService;
import faang.school.analytics.model.dto.LikeEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
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
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class LikeEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Spy
    private AnalyticsEventMapper mapper;

    @InjectMocks
    private LikeEventListener likeEventListener;

    @Test
    void testOnMessage_Success() throws Exception {

        Long postId = 1L;
        Long authorId = 2L;
        Long userId = 3L;
        LocalDateTime timestamp = LocalDateTime.now();
        LikeEventDto likeEventDto = new LikeEventDto(postId, authorId, userId, timestamp);

        String messageBody = "{\"postId\":" + postId + ", \"authorId\":" + authorId + ", \"userId\":" + userId + ", \"timestamp\":\"" + timestamp + "\"}";
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(messageBody.getBytes());

        when(objectMapper.readValue(any(byte[].class), eq(LikeEventDto.class))).thenReturn(likeEventDto);

        likeEventListener.onMessage(message, null);

        ArgumentCaptor<AnalyticsEvent> eventCaptor = ArgumentCaptor.forClass(AnalyticsEvent.class);
        verify(analyticsEventService, times(1)).saveEvent(eventCaptor.capture());

        AnalyticsEvent savedEvent = eventCaptor.getValue();
        assertEquals(postId, savedEvent.getReceiverId());
        assertEquals(userId, savedEvent.getActorId());
        assertEquals(EventType.POST_LIKE, savedEvent.getEventType());
        assertEquals(timestamp, savedEvent.getReceivedAt());
    }

    @Test
    void testOnMessage_throwRuntimeException() throws Exception {

        Message message = mock(Message.class);
        when(message.getBody()).thenReturn("invalid".getBytes());

        when(objectMapper.readValue(any(byte[].class), eq(LikeEventDto.class))).thenThrow(new IOException());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> likeEventListener.onMessage(message, null));

        verify(objectMapper, times(1)).readValue(message.getBody(), LikeEventDto.class);
        verifyNoMoreInteractions(mapper, analyticsEventService);
    }
}

