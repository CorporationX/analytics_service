package faang.school.analytics.listener.following;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.domain.dto.events.analytic.AnalyticsEventDto;
import faang.school.analytics.domain.dto.events.following.FollowerEvent;
import faang.school.analytics.mapper.following.FollowerEventMapperImpl;
import faang.school.analytics.service.events.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Spy
    private FollowerEventMapperImpl mapper;
    @InjectMocks
    FollowerEventListener eventListener;

    @Test
    void testOnMessagePositiveTest() throws IOException {
        FollowerEvent event = prepareEvent();
        AnalyticsEventDto mappedEvent = mapper.toCommonEvent(event);

        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), FollowerEvent.class)).thenReturn(event);
        when(message.getBody()).thenReturn(messageBody);
        when(analyticsEventService.saveEvent(mappedEvent)).thenReturn(mappedEvent);

        eventListener.onMessage(message, messageBody);

        verify(analyticsEventService).saveEvent(mappedEvent);
    }

    @Test
    void testOnMessageExceptionTest() throws IOException {
        FollowerEvent event = prepareEvent();
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), FollowerEvent.class)).thenThrow(JsonProcessingException.class);
        when(message.getBody()).thenReturn(messageBody);

        assertThrows(RuntimeException.class, () -> eventListener.onMessage(message, messageBody));
    }

    private FollowerEvent prepareEvent() {
        return FollowerEvent.builder()
                .followerUserId(1)
                .targetUserId(1L)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
