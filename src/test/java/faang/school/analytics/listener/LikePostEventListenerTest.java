package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.LikePostEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.hibernate.MappingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikePostEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    private LikePostEventListener likePostEventListener;
    private LikePostEventDto likePostEventDto;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() {
        likePostEventDto = LikePostEventDto.builder()
                .authorPostId(1L)
                .likedUserId(2L)
                .postId(1L)
                .build();

        analyticsEvent = AnalyticsEvent.builder()
                .receiverId(likePostEventDto.getAuthorPostId())
                .actorId(likePostEventDto.getLikedUserId())
                .eventType(EventType.POST_LIKE)
                .build();


        likePostEventListener = new LikePostEventListener(
                analyticsEventService,
                objectMapper,
                analyticsEventMapper
        );
    }

    @Test
    void testOnMessage_Success() throws Exception {

        Message message = mock(Message.class);
        String jsonMessage = "{\"authorPostId\":1,\"likedUserId\":2,\"postId\":1}";
        when(message.getBody())
                .thenReturn(jsonMessage.getBytes());


        when(objectMapper.readValue(jsonMessage.getBytes(), LikePostEventDto.class))
                .thenReturn(likePostEventDto);
        doNothing().when(analyticsEventService).saveEvent(analyticsEvent);
        when(analyticsEventMapper.toAnalyticsLikePostEvent(likePostEventDto))
                .thenReturn(analyticsEvent);

        likePostEventListener.onMessage(message, null);

        ArgumentCaptor<AnalyticsEvent> captor = ArgumentCaptor.forClass(AnalyticsEvent.class);
        verify(analyticsEventService, times(1)).saveEvent(captor.capture());

        AnalyticsEvent capturedEvent = captor.getValue();
        assertEquals(analyticsEvent, capturedEvent);
    }

    @Test
    void processEventThrowsIOExceptionTest() throws IOException {
        Message message = mock(Message.class);

        when(objectMapper.readValue(message.getBody(), LikePostEventDto.class)).
                thenThrow(IOException.class);

        assertThrows(MappingException.class, () ->
                likePostEventListener.processEvent(message, LikePostEventDto.class, event -> {
                })
        );
    }

















}
