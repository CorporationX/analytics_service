package faang.school.analytics.listener.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.like.LikeEventDto;
import faang.school.analytics.mapper.event.LikeEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.analytics_event.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikeEventListenerTest {

    private static final long POST_AUTHOR_ID_ONE = 1L;
    private static final long LIKER_ID = 1L;
    @InjectMocks
    private LikeEventListener likeEventListener;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private LikeEventMapper likeEventMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    private LikeEventDto likeEventDto;
    private Message message;


    @BeforeEach
    void setUp() {
        likeEventDto = LikeEventDto.builder()
                .postAuthorId(POST_AUTHOR_ID_ONE)
                .likerId(LIKER_ID)
                .eventType(EventType.POST_LIKE)
                .build();
        message = mock(Message.class);
    }

    @Test
    @DisplayName("When json object passed readValue, map to dto and save it")
    public void whenJsonStringPassedThenFilterReadItsValueAndSaveIt() throws IOException {
        when(message.getBody()).thenReturn(new byte[0]);
        when(objectMapper.readValue(any(byte[].class), eq(LikeEventDto.class))).thenReturn(likeEventDto);

        likeEventListener.onMessage(message, null);

        verify(objectMapper).readValue(any(byte[].class), eq(LikeEventDto.class));
        verify(likeEventMapper).fromLikeEventDtoToEntity(likeEventDto);
        verify(analyticsEventService).saveEvent(likeEventMapper.fromLikeEventDtoToEntity(likeEventDto));
    }

    @Test
    @DisplayName("If IOException while reading then throw exception")
    void whenIOExceptionOccursThenThrowsException() throws Exception {
        when(message.getBody()).thenReturn(new byte[0]);
        when(objectMapper.readValue(any(byte[].class), eq(LikeEventDto.class))).thenThrow(new IOException());
        assertThrows(RuntimeException.class, () -> likeEventListener.onMessage(message, null));
        verify(analyticsEventService, never()).saveEvent(any());
    }
}
