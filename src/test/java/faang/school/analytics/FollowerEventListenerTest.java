package faang.school.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.listener.FollowerEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowerEventListenerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventMapperImpl analyticsEventMapper;
    @Mock
    private Message message;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private FollowerEventListener followerEventListener;

    private FollowerEventDto followerEventDto;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    public void init() throws IOException {
        followerEventListener = new FollowerEventListener(objectMapper, analyticsEventService, analyticsEventMapper);

        analyticsEvent = new AnalyticsEvent();
        analyticsEvent.setId(1L);
        analyticsEvent.setActorId(1L);
        analyticsEvent.setReceiverId(10L);
        analyticsEvent.setEventType(EventType.FOLLOWER);

        followerEventDto = new FollowerEventDto();
        followerEventDto.setFollowerId(1L);
        followerEventDto.setFolloweeId(10L);

        when(message.getBody()).thenReturn(new byte[0]);
        when(objectMapper.readValue(message.getBody(), FollowerEventDto.class)).thenReturn(followerEventDto);
        when(analyticsEventMapper.toEntity(followerEventDto)).thenReturn(analyticsEvent);
    }

    @Test
    public void onMessageTest() throws IOException {

        followerEventListener.onMessage(message, null);

        verify(objectMapper).readValue(message.getBody(), FollowerEventDto.class);
        verify(analyticsEventMapper).toEntity(followerEventDto);
    }

    @Test
    public void onMessageCallsServiceMethod() {
        followerEventListener.onMessage(message, null);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);

    }


}
