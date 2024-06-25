package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentEventEventListenerTest {

    @InjectMocks
    private CommentEventEventListener commentEventEventListener;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventMapperImpl analyticsEventMapper;

    @Test
    public void testOnMessage() {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        CommentEventDto commentEventDto = CommentEventDto.builder().postId(1L).build();
        Message message = mock(Message.class);

        when(message.getBody()).thenReturn("{\"id\":\"1\"}".getBytes());
        try {
            when(objectMapper.readValue(message.getBody(), CommentEventDto.class)).thenReturn(commentEventDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        when(analyticsEventMapper.toCommentEntity(commentEventDto)).thenReturn(analyticsEvent);

        commentEventEventListener.onMessage(message, null);

        verify(analyticsEventMapper, times(1)).toCommentEntity(commentEventDto);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
    }
}
