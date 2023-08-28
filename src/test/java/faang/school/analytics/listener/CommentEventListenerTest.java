package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import org.junit.jupiter.api.extension.ExtendWith;
import java.io.IOException;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private Message message;

    @InjectMocks
    private CommentEventListener commentEventListener;

    @Test
    void onMessage_ValidMessage_SuccessfulProcessing() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        LocalDateTime timeNow = LocalDateTime.now();

        CommentEventDto commentEventDto = CommentEventDto.builder()
                .postId(1L)
                .authorId(2L)
                .commentId(3L)
                .createdAt(timeNow)
                .build();

        String json = objectMapper.writeValueAsString(commentEventDto);

        byte[] bodyBytes = json.getBytes();

        when(message.getBody()).thenReturn(bodyBytes);
        when(analyticsEventMapper.toEntity(commentEventDto)).thenReturn(new AnalyticsEvent());

        commentEventListener.onMessage(message, null);

        verify(analyticsEventService).save(any(AnalyticsEvent.class));
        verify(analyticsEventMapper).toEntity(commentEventDto);
    }

    @Test
    void onMessage_InvalidMessage_LogsError() throws IOException {
        String invalidJson = "invalid-json";
        byte[] invalidJsonBytes = invalidJson.getBytes();

        doReturn(invalidJsonBytes).when(message).getBody();

        commentEventListener.onMessage(message, null);
    }
}