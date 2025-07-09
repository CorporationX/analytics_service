package faang.school.analytics.service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.FollowerEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class FollowerEventListenerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Spy
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private faang.school.analytics.listener.FollowerEventListener listener;

    @Test
    void testOnMessage_savesMappedEvent() throws Exception {
        FollowerEvent followerEvent = new FollowerEvent();
        followerEvent.setFollowerId(1L);
        followerEvent.setPublisherId(2L);
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] json = objectMapper.writeValueAsBytes(followerEvent);
        Message redisMessage = mock(Message.class);
        when(redisMessage.getBody()).thenReturn(json);
        AnalyticsEvent mappedEvent = new AnalyticsEvent();
        when(analyticsEventMapper.toEntity(any(FollowerEvent.class))).thenReturn(mappedEvent);

        listener.onMessage(redisMessage, null);

        verify(analyticsEventMapper).toEntity(any(FollowerEvent.class));
        verify(analyticsEventService).save(mappedEvent);
    }
}
