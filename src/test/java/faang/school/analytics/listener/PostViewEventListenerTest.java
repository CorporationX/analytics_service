package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.post.PostViewEvent;
import faang.school.analytics.listener.post.PostViewEventListener;
import faang.school.analytics.mapper.analytics.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
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
import java.time.LocalDateTime;
import java.time.Month;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostViewEventListenerTest {

    @InjectMocks
    private PostViewEventListener postViewEventListener;

    @Mock
    private Message message;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    private static final Long ID = 1L;
    private PostViewEvent postViewEvent;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    public void init() {
        postViewEventListener = new PostViewEventListener(objectMapper, analyticsEventMapper, analyticsEventService);

        postViewEvent = new PostViewEvent();
        postViewEvent.setActorId(ID);
        postViewEvent.setReceiverId(ID);
        postViewEvent.setReceivedAt(LocalDateTime.of(2024, Month.AUGUST, 10, 10, 10));

        analyticsEvent = AnalyticsEvent.builder()
                .receiverId(ID)
                .actorId(ID)
                .receivedAt(LocalDateTime.of(2024, Month.AUGUST, 10, 10, 10))
                .eventType(EventType.POST_VIEW)
                .build();
    }

    @Test
    @DisplayName("Успешное сохранение AnalyticsEvent")
    public void whenOnMessageWithCurrentDataThenSuccess() throws IOException {
        when(objectMapper.readValue(message.getBody(), PostViewEvent.class)).thenReturn(postViewEvent);
        when(analyticsEventMapper.toAnalyticsEntity(postViewEvent)).thenReturn(analyticsEvent);

        postViewEventListener.onMessage(message, new byte[0]);

        verify(objectMapper).readValue(message.getBody(), PostViewEvent.class);
        verify(analyticsEventMapper).toAnalyticsEntity(postViewEvent);
        verify(analyticsEventService).saveEvent(any(AnalyticsEvent.class));
    }
}