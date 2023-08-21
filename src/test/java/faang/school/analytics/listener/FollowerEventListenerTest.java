package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.mapper.FollowerEventMapper;
import faang.school.analytics.mapper.FollowerEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerEventListenerTest {

    @Spy
    private FollowerEventMapper followerEventMapper = new FollowerEventMapperImpl();
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private Message message;
    @InjectMocks
    private FollowerEventListener followerEventListener;

    @Test
    void onMessageTest() throws IOException {
        LocalDateTime currentTime = LocalDateTime.now();
        FollowerEventDto dto = FollowerEventDto.builder()
                .followerId(1)
                .followeeId(2)
                .subscriptionTime(currentTime)
                .build();
        AnalyticsEvent event = AnalyticsEvent.builder()
                .actorId(1)
                .receiverId(2)
                .eventType(EventType.FOLLOWER)
                .receivedAt(currentTime)
                .build();

        when(message.getBody()).thenReturn(new byte[0]);
        when(objectMapper.readValue(new byte[0], FollowerEventDto.class)).thenReturn(dto);

        followerEventListener.onMessage(message, new byte[0]);

        verify(objectMapper).readValue(new byte[0], FollowerEventDto.class);
        verify(analyticsEventService).saveEvent(event);
    }
}