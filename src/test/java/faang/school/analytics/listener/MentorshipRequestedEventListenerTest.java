package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.MentorshipRequestedEventDto;
import faang.school.analytics.mapper.MentorshipRequestedEventMapper;
import faang.school.analytics.mapper.MentorshipRequestedEventMapperImpl;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentorshipRequestedEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Spy
    private MentorshipRequestedEventMapper mentorshipRequestedEventMapper = new MentorshipRequestedEventMapperImpl();
    @Mock
    private Message message;

    @InjectMocks
    private MentorshipRequestedEventListener eventListener;

    @Test
    void testOnMessage() throws IOException {

        byte[] pattern = new byte[0];
        LocalDateTime timeNow = LocalDateTime.now();

        MentorshipRequestedEventDto mentorshipRequestedEventDto = MentorshipRequestedEventDto.builder()
                .requesterId(1)
                .receiverId(2)
                .createdAt(timeNow)
                .build();

        AnalyticsEventDto analyticsEventDto = AnalyticsEventDto.builder()
                .actorId(1)
                .receiverId(2)
                .eventType(EventType.MENTORSHIP_REQUESTED)
                .receivedAt(timeNow)
                .build();

        when(message.getBody()).thenReturn(pattern);
        when(objectMapper.readValue(pattern, MentorshipRequestedEventDto.class))
                .thenReturn(mentorshipRequestedEventDto);
        when(mentorshipRequestedEventMapper.toDto(mentorshipRequestedEventDto))
                .thenReturn(analyticsEventDto);

        eventListener.onMessage(message, pattern);

        verify(objectMapper).readValue(pattern, MentorshipRequestedEventDto.class);
        verify(mentorshipRequestedEventMapper).toDto(mentorshipRequestedEventDto);
        verify(analyticsEventService).save(analyticsEventDto);
    }
}