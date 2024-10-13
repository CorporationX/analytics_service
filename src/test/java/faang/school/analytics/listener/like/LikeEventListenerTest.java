package faang.school.analytics.listener.like;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikeEventListenerTest {

    private static final String JSON_STRING_FROM_POST_SERVICE =
            "{\"postAuthorId\":1,\"likerId\":1,\"eventType\":\"POST_LIKE\"}";
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


    @BeforeEach
    void setUp() {
        likeEventDto = LikeEventDto.builder()
                .postAuthorId(POST_AUTHOR_ID_ONE)
                .likerId(LIKER_ID)
                .eventType(EventType.POST_LIKE)
                .build();
    }

    @Test
    @DisplayName("When Json string passed filter with regex, readValue to dto, save it")
    public void whenJsonStringPassedThenFilterReadItsValueAndSaveIt() throws JsonProcessingException {
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(JSON_STRING_FROM_POST_SERVICE.getBytes(StandardCharsets.UTF_8));
        when(message.getChannel()).thenReturn(new byte[]{});
        when(objectMapper.readValue(JSON_STRING_FROM_POST_SERVICE, LikeEventDto.class)).thenReturn(likeEventDto);

        assertDoesNotThrow(() -> likeEventListener.onMessage(message, new byte[]{}));
        verify(analyticsEventService).saveEvent(likeEventMapper.fromLikeEventDtoToEntity(likeEventDto));
    }
}
