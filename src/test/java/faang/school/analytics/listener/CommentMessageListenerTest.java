package faang.school.analytics.listener;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.dto.EventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.JsonObjectMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentMessageListenerTest {

    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private JsonObjectMapper jsonObjectMapper;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @InjectMocks
    private CommentMessageListener listener;
    private CommentEventDto commentEventDto;
    private EventDto eventDto;
    private Message message;
    private String body;
    private byte[] pattern;

    @BeforeEach
    public void setUp() {
        commentEventDto = CommentEventDto.builder().build();
        eventDto = EventDto.builder().build();
        body = "body";
        pattern = new byte[]{1};
        message = new DefaultMessage(pattern, body.getBytes());
    }

    @Test
    void testOnMessage() {
        when(jsonObjectMapper.readValue(message.getBody(), CommentEventDto.class)).thenReturn(commentEventDto);
        when(analyticsEventMapper.toEventDto(commentEventDto)).thenReturn(eventDto);

        listener.onMessage(message, pattern);

        verify(analyticsEventMapper).toEventDto(commentEventDto);
        verify(analyticsEventService).saveEvent(eventDto);
        assertEquals(eventDto.getEventType(), EventType.POST_COMMENT);
    }
}