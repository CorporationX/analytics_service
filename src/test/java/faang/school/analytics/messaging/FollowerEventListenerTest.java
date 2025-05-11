package faang.school.analytics.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowerEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private FollowerEventListener followerEventListener;

    @Test
    void testOnMessageSuccess() throws Exception {
        FollowerEvent event = new FollowerEvent(1L, 2L);
        byte[] mockBody = "mocked body".getBytes();
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(mockBody);

        when(objectMapper.readValue(mockBody, FollowerEvent.class)).thenReturn(event);
        when(analyticsEventMapper.toAnalyticsEventDto(event)).thenReturn(new AnalyticsEventDto());
        when(analyticsEventService.saveEvent(any())).thenReturn(new AnalyticsEventDto());

        followerEventListener.onMessage(message, null);

        verify(objectMapper).readValue(mockBody, FollowerEvent.class);
        verify(analyticsEventMapper).toAnalyticsEventDto(event);
        verify(analyticsEventService).saveEvent(any());
    }

}
