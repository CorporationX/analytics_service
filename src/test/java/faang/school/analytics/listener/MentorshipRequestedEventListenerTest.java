package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.dto.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.dto.mentorship.MentorshipRequestEvent;
import faang.school.analytics.exception.DataTransformationException;
import faang.school.analytics.mapper.MentorshipRequestEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.analyticsEvent.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MentorshipRequestedEventListenerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private MentorshipRequestEventMapper mentorshipRequestEventMapper;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private MentorshipRequestedEventListener mentorshipRequestedEventListener;
    @Mock
    private Message message;
    byte[] pattern = new byte[]{};
    private MentorshipRequestEvent mentorshipRequestEvent;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void init() {
        LocalDateTime localDateTime = LocalDateTime.now();
        mentorshipRequestEvent = MentorshipRequestEvent.builder()
                .requesterId(1L)
                .receiverId(2L)
                .timestamp(localDateTime)
                .build();

        analyticsEvent = AnalyticsEvent.builder()
                .id(1L)
                .actorId(1L)
                .receiverId(2L)
                .eventType(EventType.MENTORSHIP_RECEIVED)
                .receivedAt(localDateTime)
                .build();
    }

    @Test
    @DisplayName("readValueException")
    void testReadValueException() throws IOException {
        Mockito.when(objectMapper.readValue(Mockito.any(byte[].class), Mockito.eq(MentorshipRequestEvent.class)))
                .thenThrow(new IOException("Failed to convert message to objectMapper."));

        DataTransformationException exception = assertThrows(DataTransformationException.class, () ->
                mentorshipRequestedEventListener.onMessage(message, pattern));
        assertEquals("Failed to convert message to objectMapper.", exception.getMessage());
    }

    @Test
    @DisplayName("toEntityValid")
    void testToEntityValid() throws IOException {
        Mockito.when(objectMapper.readValue(Mockito.any(byte[].class), Mockito.eq(MentorshipRequestEvent.class)))
                .thenReturn(mentorshipRequestEvent);
        Mockito.when(mentorshipRequestEventMapper.toEntity(Mockito.any(MentorshipRequestEvent.class)))
                .thenReturn(analyticsEvent);

        byte[] mockMessageBody = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsBytes(mentorshipRequestEvent);
        Mockito.when(message.getBody()).thenReturn(mockMessageBody);

        mentorshipRequestedEventListener.onMessage(message, pattern);

        Mockito.verify(objectMapper, Mockito.times(1))
                .readValue(Mockito.any(byte[].class), Mockito.eq(MentorshipRequestEvent.class));
        Mockito.verify(mentorshipRequestEventMapper, Mockito.times(1))
                .toEntity(Mockito.any(MentorshipRequestEvent.class));
    }

    @Test
    @DisplayName("saveValid")
    void testSaveValid() throws IOException {
        Mockito.when(objectMapper.readValue(Mockito.any(byte[].class), Mockito.eq(MentorshipRequestEvent.class)))
                .thenReturn(mentorshipRequestEvent);
        Mockito.when(mentorshipRequestEventMapper.toEntity(Mockito.any(MentorshipRequestEvent.class)))
                .thenReturn(analyticsEvent);
        Mockito.when(analyticsEventService.saveEvent(Mockito.any(AnalyticsEvent.class)))
                .thenReturn(new AnalyticsEventDto());

        byte[] mockMessageBody = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsBytes(mentorshipRequestEvent);
        Mockito.when(message.getBody()).thenReturn(mockMessageBody);

        mentorshipRequestedEventListener.onMessage(message, pattern);

        Mockito.verify(objectMapper, Mockito.times(1))
                .readValue(Mockito.any(byte[].class), Mockito.eq(MentorshipRequestEvent.class));
        Mockito.verify(mentorshipRequestEventMapper, Mockito.times(1))
                .toEntity(Mockito.any(MentorshipRequestEvent.class));
        Mockito.verify(analyticsEventService, Mockito.times(1)).saveEvent(Mockito.any(AnalyticsEvent.class));
    }
}