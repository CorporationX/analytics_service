package faang.school.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.listener.PostViewEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostViewEventListenerTest {

    @InjectMocks
    private PostViewEventListener postViewEventListener;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Spy
    private ObjectMapper objectMapper;

    @Test
    public void testOnMessage_successfullyProceed() throws Exception {
        String channel = "test_channel";
        String jsonBody = createJsonBody(100, 200, 300, LocalDateTime.parse("2024-08-26T16:05:00"));

        PostViewEventDto postViewEventDto = new PostViewEventDto();
        postViewEventDto.setPostId(100);
        postViewEventDto.setReceiverId(200);
        postViewEventDto.setActorId(300);
        postViewEventDto.setReceivedAt(LocalDateTime.parse("2024-08-26T16:05:00"));

        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        doReturn(postViewEventDto).when(objectMapper).readValue(jsonBody, PostViewEventDto.class);
        when(analyticsEventMapper.postViewEventToEntity(postViewEventDto)).thenReturn(analyticsEvent);

        postViewEventListener.onMessage(createTestMessage(channel, jsonBody), null);

        verify(analyticsEventService, times(1)).save(analyticsEvent);


    }

    @Test
    void testOnMessage_withArgumentCaptor() throws Exception {
        // Arrange
        String channel = "test-channel";
        String jsonBody = createJsonBody(1000, 2000, 3000, LocalDateTime.parse("2024-08-26T16:05:00"));

        PostViewEventDto postViewEventDto = new PostViewEventDto();
        postViewEventDto.setPostId(1000);
        postViewEventDto.setReceiverId(2000);
        postViewEventDto.setActorId(3000);
        postViewEventDto.setReceivedAt(LocalDateTime.parse("2024-08-26T16:05:00"));

        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        doReturn(postViewEventDto).when(objectMapper).readValue(jsonBody, PostViewEventDto.class);
        when(analyticsEventMapper.postViewEventToEntity(postViewEventDto)).thenReturn(analyticsEvent);

        ArgumentCaptor<PostViewEventDto> dtoCaptor = ArgumentCaptor.forClass(PostViewEventDto.class);

        postViewEventListener.onMessage(createTestMessage(channel, jsonBody), null);

        // Assert
        verify(analyticsEventMapper, times(1)).postViewEventToEntity(dtoCaptor.capture());
        verify(analyticsEventService, times(1)).save(analyticsEvent);

        PostViewEventDto capturedDto = dtoCaptor.getValue();
        assertEquals(1000, capturedDto.getPostId());
        assertEquals(2000, capturedDto.getReceiverId());
        assertEquals(3000, capturedDto.getActorId());
        assertEquals(LocalDateTime.parse("2024-08-26T16:05:00"), capturedDto.getReceivedAt());
    }

    @Test
    void testOnMessage_jsonProcessingException() throws Exception {

        String channel = "test-channel";
        String jsonBody = "invalid json";

        Assertions.assertThrows(RuntimeException.class, () -> {
            postViewEventListener.onMessage(createTestMessage(channel, jsonBody), null);
        });
    }

    private Message createTestMessage(String channel, String body) {
        Message message = mock(Message.class);
        when(message.getChannel()).thenReturn(channel.getBytes());
        when(message.getBody()).thenReturn(body.getBytes(StandardCharsets.UTF_8));
        return message;

    }

    private String createJsonBody(long postId, long receiverId, long actorId, LocalDateTime receivedAt) {
        return "{"
               + "\"postId\": " + postId + ","
               + "\"userId\": " + receiverId + ","
               + "\"authorId\": " + actorId + ","
               + "\"viewedAt\": \"" + receivedAt + "\""
               + "}";
    }
}
