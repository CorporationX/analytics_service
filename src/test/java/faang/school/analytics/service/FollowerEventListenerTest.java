package faang.school.analytics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.dto.UserDto;
import faang.school.analytics.listener.FollowerEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class FollowerEventListenerTest {
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private Message message;
    @InjectMocks
    private FollowerEventListener followEventListener;

    private FollowerEvent followerEvent;
    private AnalyticsEvent analyticsEvent;
    private UserDto follower;
    private UserDto followee;
    private byte[] pattern;

    @BeforeEach
    public void setUp() {
        followerEvent = FollowerEvent.builder().followerId(1L).followeeId(2L).
                subscriptionTime(LocalDateTime.of(2024,8, 17, 0, 0)).build();
        analyticsEvent = AnalyticsEvent.builder().id(0L).actorId(1L).receiverId(2L).eventType(EventType.FOLLOWER)
                .receivedAt(LocalDateTime.of(2024,8, 17, 0, 0)).build();
        follower = new UserDto(1L, "Andrey", "example@mail.ru");
        followee = new UserDto(2L, "Alexei", "another@mail.ru");
        pattern = new byte[]{};
    }

    @Test
    public void onMessageTest() throws IOException {
        Mockito.doNothing().when(analyticsEventService).save(analyticsEvent);
        Mockito.when(analyticsEventMapper.toEntity(followerEvent)).thenReturn(analyticsEvent);
        Mockito.when(objectMapper.readValue(message.getBody(), FollowerEvent.class)).thenReturn(followerEvent);
        followEventListener.onMessage(message, pattern);
        Mockito.verify(analyticsEventService).save(analyticsEvent);
        Mockito.verify(objectMapper).readValue(message.getBody(), FollowerEvent.class);
    }
}
