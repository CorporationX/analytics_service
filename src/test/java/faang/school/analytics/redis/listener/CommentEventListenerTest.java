package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.AnalyticsEventMapper;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private AnalyticsEventRepository repository;
    @Mock
    private Message message;
    private CommentEventDto commentEventDto;
    private AnalyticsEvent analyticsEvent;
    @InjectMocks
    private CommentEventListener commentEventListener;

    @BeforeEach
    void setUp() throws IOException {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        commentEventDto = CommentEventDto.builder()
                .authorId(2L)
                .createdAt(dateTime)
                .build();

        analyticsEvent = AnalyticsEvent.builder()
                .id(1L)
                .actorId(2L)
                .eventType(EventType.POST_COMMENT)
                .receivedAt(dateTime)
                .build();

        when(message.getBody()).thenReturn(new byte[0]);
        when(objectMapper.readValue(message.getBody(), CommentEventDto.class)).thenReturn(commentEventDto);
        when(analyticsEventMapper.toCommentEntity(commentEventDto)).thenReturn(analyticsEvent);
    }
    @Test
    void testOnMessage_SaveCommentEvent() throws IOException {
        commentEventListener.onMessage(message, null);

        verify(objectMapper).readValue(eq(message.getBody()), eq(CommentEventDto.class));
        verify(analyticsEventMapper).toCommentEntity(commentEventDto);
        verify(repository).save(analyticsEvent);
    }

    @Test
    void onMessage_ValidMessage_SavesCommentEvent() throws IOException {
        when(message.getBody()).thenReturn(new byte[0]);

        commentEventListener.onMessage(message, new byte[0]);

        verify(objectMapper).readValue(message.getBody(), CommentEventDto.class);
        verify(analyticsEventMapper).toCommentEntity(eq(commentEventDto));
        verify(repository).save(eq(analyticsEvent));
    }

    @Test
    void testOnMessage_SuccessfulHandling() {
        commentEventListener.onMessage(message, new byte[0]);

        verify(repository, times(1)).save(eq(analyticsEvent));
    }
}