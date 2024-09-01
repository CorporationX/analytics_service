package faang.school.analytics.messaging.listener.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.like.LikeEvent;
import faang.school.analytics.exception.event.DataTransformationException;
import faang.school.analytics.mapper.like.LikeEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.analytics.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeEventListenerTest {

    @InjectMocks
    private LikeEventListener likeEventListener;

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private Message message;
    @Mock
    private LikeEventMapperImpl likeEventMapper = new LikeEventMapperImpl();

    @Test
    void testOnMessageShouldThrowException() throws IOException {
        String invalidJson = "Invalid json";

        when(message.getBody()).thenReturn(invalidJson.getBytes());
        when(objectMapper.readValue(invalidJson.getBytes(), LikeEvent.class)).thenThrow(IOException.class);

        assertThrows(DataTransformationException.class, () -> likeEventListener.onMessage(message, new byte[0]));
        verifyNoInteractions(analyticsEventService, likeEventMapper);
    }

    @Test
    void testOnMessage() throws IOException {
        UUID eventId = UUID.randomUUID();
        var likeEvent = LikeEvent.builder()
                .eventId(eventId)
                .authorId(1)
                .postId(1)
                .timeStamp(LocalDateTime.of(2021, 1, 1, 0, 0))
                .likeId(1)
                .build();
        var analyticsEvent = AnalyticsEvent.builder()
                .eventId(eventId)
                .receiverId(1)
                .actorId(1)
                .receivedAt(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();
        String jsonMessage = String.format(
                "{\"eventId\":\"%s\",\"authorId\":1,\"postId\":1,\"likeId:1,\"timestamp\":\"2021-01-01T00:00:00\"}",
                eventId
        );

        when(message.getBody()).thenReturn(jsonMessage.getBytes());
        when(objectMapper.readValue(jsonMessage.getBytes(), LikeEvent.class)).thenReturn(likeEvent);
        when(likeEventMapper.toAnalyticsEventEntity(likeEvent)).thenReturn(analyticsEvent);

        likeEventListener.onMessage(message, new byte[0]);

        verify(objectMapper, times(1)).readValue(jsonMessage.getBytes(), LikeEvent.class);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);

        assertEquals(EventType.POST_LIKE, analyticsEvent.getEventType());
    }
}
