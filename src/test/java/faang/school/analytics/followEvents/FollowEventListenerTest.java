package faang.school.analytics.followEvents;

import faang.school.analytics.dto.followEvent.FollowEventDto;
import faang.school.analytics.messaging.followEvent.FollowEventListener;
import faang.school.analytics.service.follow.FollowEventWorker;
import faang.school.analytics.util.Mapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

@ExtendWith(MockitoExtension.class)
public class FollowEventListenerTest {

    @Mock
    Message message;

    @Mock
    Mapper mapper;

    @Mock
    FollowEventWorker followEventWorker;

    @InjectMocks
    FollowEventListener followEventListener;

    @Test
    void messageReceiveTest() {
        FollowEventDto FollowerEventDto = new FollowEventDto();

        Mockito.when(message.toString()).thenReturn("s");
        Mockito.when(mapper.toObject(Mockito.anyString(), Mockito.eq(FollowEventDto.class)))
                .thenReturn(FollowerEventDto);

        followEventListener.onMessage(message, "".getBytes());

        Mockito.verify(followEventWorker, Mockito.times(1)).saveFollowEvent(FollowerEventDto);
    }
}
