package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.MentorshipRequestedEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentorshipRequestedEventListenerTest {
    @InjectMocks
    private MentorshipRequestedEventListener mentorshipRequestedEventListener;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private AnalyticsEventRepository repository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private Message message;
    private MentorshipRequestedEventDto mentorshipRequestedEventDto;
    private AnalyticsEvent analyticsEvent;
    private byte[] pattern;

    @BeforeEach
    void setUp() throws IOException {
        LocalDateTime timeNow = LocalDateTime.now();
        pattern = new byte[0];
        mentorshipRequestedEventDto = MentorshipRequestedEventDto.builder()
                .requesterId(1)
                .receiverId(2)
                .createdAt(timeNow)
                .build();
        analyticsEvent = AnalyticsEvent.builder()
                .actorId(1)
                .receiverId(2)
                .eventType(EventType.MENTORSHIP_REQUESTED)
                .receivedAt(timeNow)
                .build();
        when(message.getBody()).thenReturn(pattern);
        when(objectMapper.readValue(pattern, MentorshipRequestedEventDto.class))
                .thenReturn(mentorshipRequestedEventDto);
        when(analyticsEventMapper.toMentorshipRequestedEntity(mentorshipRequestedEventDto))
                .thenReturn(analyticsEvent);
    }

    @Test
    void testOnMessage() throws IOException {
        mentorshipRequestedEventListener.onMessage(message, pattern);

        verify(objectMapper).readValue(message.getBody(), MentorshipRequestedEventDto.class);
        verify(analyticsEventMapper).toMentorshipRequestedEntity(mentorshipRequestedEventDto);
        verify(repository).save(analyticsEvent);
    }
}