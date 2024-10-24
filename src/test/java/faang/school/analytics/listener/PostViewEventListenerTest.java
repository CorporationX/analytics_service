package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.dto.event.PostViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostViewEventListenerTest {

    private ObjectMapper objectMapper;

    @Spy
    private AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);

    @Mock
    private AnalyticsEventServiceImpl analyticsEventService;

    private PostViewEventListener postViewEventListener;

    private Message message;

    @BeforeEach
    void setUp() {
        objectMapper = Mockito.spy(getMapper());
        postViewEventListener = new PostViewEventListener(
                objectMapper,
                analyticsEventMapper,
                analyticsEventService);
        String event = "{\"postId\":1,\"authorId\":1,\"viewerId\":2,\"timestamp\":\"2024-10-17T12:34:56\"}";
        message = new DefaultMessage("post_view_channel".getBytes(), event.getBytes());
    }

    @Test
    @DisplayName("Process message")
    void postViewEventListenerTest_processMessage() throws IOException {
        postViewEventListener.onMessage(message, null);

        verify(objectMapper).readValue(message.getBody(), PostViewEventDto.class);
        verify(analyticsEventMapper).toAnalyticsEvent(any());
        verify(analyticsEventService).saveEvent(any());
    }

    @Test
    @DisplayName("Process message with exception")
    void postViewEventListenerTest_processMessageWithException() throws IOException {
        IOException exception = new IOException("exception");
        doThrow(exception).when(objectMapper).readValue(message.getBody(), PostViewEventDto.class);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> postViewEventListener.onMessage(message, null));

        assertEquals(exception.toString(), ex.getMessage());
        verify(objectMapper).readValue(message.getBody(), PostViewEventDto.class);
    }

    private ObjectMapper getMapper() {
        ObjectMapper objectMapper1 = new ObjectMapper();
        objectMapper1.registerModule(new JavaTimeModule());
        return objectMapper1;
    }
}
