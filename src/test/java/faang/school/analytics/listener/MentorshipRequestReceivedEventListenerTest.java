package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.MentorshipRequestReceivedDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MentorshipRequestReceivedEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private MentorshipRequestReceivedEventListener listener;

    private Message message;
    private MentorshipRequestReceivedDto dto;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() {
        dto = new MentorshipRequestReceivedDto();
        analyticsEvent = new AnalyticsEvent();
        message = mock(Message.class);
    }

    @Test
    void testOnMessage_Success() throws Exception {
        when(message.getBody()).thenReturn(new byte[]{});
        when(objectMapper.readValue(any(byte[].class), eq(MentorshipRequestReceivedDto.class))).thenReturn(dto);
        when(analyticsEventMapper.mentorshipRequestReceivedDtoToAnalyticsEvent(dto)).thenReturn(analyticsEvent);

        listener.onMessage(message, null);

        verify(analyticsEventService).saveEvent(analyticsEvent);
    }

    @Test
    void testOnMessage_Exception() throws Exception {
        when(message.getBody()).thenReturn(new byte[]{});
        when(objectMapper.readValue(any(byte[].class), eq(MentorshipRequestReceivedDto.class))).thenThrow(new IOException("Parsing error"));

        listener.onMessage(message, null);

        verify(analyticsEventService, never()).saveEvent(any());
    }
}