package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.analytics_event.AnalyticsEventDto;
import faang.school.analytics.dto.post.PostViewEvent;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
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
        postViewEvent = PostViewEvent.builder()
                .postId(ID)
                .authorId(ID)
                .userId(ID)
                .localDateTime(LocalDateTime.of(2024, Month.AUGUST, 10, 10, 10))
                .build();
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
        when(analyticsEventMapper.toAnalyticsEvent(postViewEvent)).thenReturn(analyticsEvent);

        postViewEventListener.onMessage(message, new byte[0]);

        verify(objectMapper).readValue(message.getBody(), PostViewEvent.class);
        verify(analyticsEventMapper).toAnalyticsEvent(postViewEvent);
        verify(analyticsEventService).saveEvent(any(AnalyticsEvent.class));
    }
}