package faang.school.analytics.listener;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.MentorshipRequestedEventDto;
import faang.school.analytics.mapper.JsonObjectMapper;
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

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentorshipRequestedEventListenerTest {

    @Mock
    private JsonObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Spy
    private MentorshipRequestedEventMapper mentorshipRequestedEventMapper = new MentorshipRequestedEventMapperImpl();
    @Mock
    private Message message;

    @InjectMocks
    private MentorshipRequestedEventListener eventListener;

    @Test
    void testOnMessage() {

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
        when(objectMapper.fromJson(pattern, MentorshipRequestedEventDto.class)).thenReturn(mentorshipRequestedEventDto);

        eventListener.onMessage(message, pattern);

        verify(objectMapper).fromJson(pattern, MentorshipRequestedEventDto.class);
        verify(mentorshipRequestedEventMapper).toDto(mentorshipRequestedEventDto);
        verify(analyticsEventService).save(analyticsEventDto);
    }
}