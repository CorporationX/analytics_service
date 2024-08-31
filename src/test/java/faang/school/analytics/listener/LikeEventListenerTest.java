package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class LikeEventListenerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private LikeEventListener likeEventListener;
    @Mock
    private Message message;
    byte[] pattern = new byte[]{};
    private LikeEvent likeEvent;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    public void setUp() {
        LocalDateTime eventAt = LocalDateTime.now();
        likeEvent = LikeEvent.builder()
                .postId(1L)
                .authorId(2L)
                .userId(3L)
                .eventAt(eventAt)
                .build();

        analyticsEvent = AnalyticsEvent.builder()
                .id(1L)
                .actorId(3L)
                .receiverId(2L)
                .eventType(EventType.POST_LIKE)
                .receivedAt(eventAt)
                .build();
    }

    @Test
    @DisplayName("readValueException")
    public void testReadValueException() throws IOException {
        Mockito.when(objectMapper.readValue(Mockito.any(byte[].class), Mockito.eq(LikeEvent.class)))
                .thenThrow(new IOException());
        byte[] mockMessageBody = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsBytes(likeEvent);
        Mockito.when(message.getBody()).thenReturn(mockMessageBody);

        assertThrows(RuntimeException.class, () ->
                likeEventListener.onMessage(message, pattern));
    }

    @Test
    @DisplayName("toEntityValid")
    public void testToEntityValid() throws IOException {
        Mockito.when(objectMapper.readValue(Mockito.any(byte[].class), Mockito.eq(LikeEvent.class)))
                .thenReturn(likeEvent);
        Mockito.when(analyticsEventMapper.toEntity(Mockito.any(LikeEvent.class)))
                .thenReturn(analyticsEvent);

        byte[] mockMessageBody = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsBytes(likeEvent);
        Mockito.when(message.getBody()).thenReturn(mockMessageBody);

        likeEventListener.onMessage(message, pattern);

        Mockito.verify(objectMapper, Mockito.times(1))
                .readValue(Mockito.any(byte[].class), Mockito.eq(LikeEvent.class));
        Mockito.verify(analyticsEventMapper, Mockito.times(1))
                .toEntity(Mockito.any(LikeEvent.class));
    }

    @Test
    @DisplayName("saveValid")
    public void testSaveValid() throws IOException {
        Mockito.when(objectMapper.readValue(Mockito.any(byte[].class), Mockito.eq(LikeEvent.class)))
                .thenReturn(likeEvent);
        Mockito.when(analyticsEventMapper.toEntity(Mockito.any(LikeEvent.class)))
                .thenReturn(analyticsEvent);

        byte[] mockMessageBody = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsBytes(likeEvent);
        Mockito.when(message.getBody()).thenReturn(mockMessageBody);

        likeEventListener.onMessage(message, pattern);

        Mockito.verify(objectMapper, Mockito.times(1))
                .readValue(Mockito.any(byte[].class), Mockito.eq(LikeEvent.class));
        Mockito.verify(analyticsEventMapper, Mockito.times(1))
                .toEntity(Mockito.any(LikeEvent.class));
        Mockito.verify(analyticsEventService, Mockito.times(1))
                .saveEvent(Mockito.any(AnalyticsEvent.class));
    }
}