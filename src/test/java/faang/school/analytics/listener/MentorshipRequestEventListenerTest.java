package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.analytics_event.AnalyticsEventDto;
import faang.school.analytics.dto.message.MentorshipRequestMessage;
import faang.school.analytics.listener.mentorshipRequest.MentorshipRequestEventListener;
import faang.school.analytics.mapper.mentorshipRequest.MentorshipRequestMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.analytics_event.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MentorshipRequestEventListenerTest {

    private final long requesterId = 1;
    private final long receiverId = 2;
    private final LocalDateTime createdAt = LocalDateTime.of(2024, 2, 5, 6, 5);


    @InjectMocks
    private MentorshipRequestEventListener mentorshipRequestEventListener;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private MentorshipRequestMapper mentorshipRequestMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private Message message;

    @Test
    @DisplayName("the method should save events to the DB")
    void testOnMessage() throws IOException {

        MentorshipRequestMessage mentorshipRequestMessage = MentorshipRequestMessage.builder()
                .requesterId(requesterId)
                .receiverId(receiverId)
                .createdAt(createdAt)
                .build();

        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .actorId(requesterId)
                .receiverId(receiverId)
                .receivedAt(createdAt)
                .build();

        AnalyticsEventDto analyticsEventDto = AnalyticsEventDto.builder()
                .actorId(requesterId)
                .receiverId(receiverId)
                .receivedAt(createdAt)
                .build();

        when(objectMapper.readValue(message.getBody(), MentorshipRequestMessage.class))
                .thenReturn(mentorshipRequestMessage);
        when(mentorshipRequestMapper.toAnalyticsEvent(mentorshipRequestMessage)).thenReturn(analyticsEvent);
        when(analyticsEventService.saveEvent(analyticsEvent)).thenReturn(analyticsEventDto);

        mentorshipRequestEventListener.onMessage(message, new byte[0]);

        verify(objectMapper).readValue(message.getBody(), MentorshipRequestMessage.class);
        verify(mentorshipRequestMapper).toAnalyticsEvent(mentorshipRequestMessage);
        verify(analyticsEventService).saveEvent(analyticsEvent);

        assertDoesNotThrow(() -> mentorshipRequestEventListener.onMessage(message, new byte[0]));
    }
}