package faang.school.analytics.messaging;

import faang.school.analytics.dto.LikeEventDto;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.util.JsonMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class LikeEventListenerTest {

    @Mock
    private JsonMapper jsonMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private Message message;
    @InjectMocks
    private LikeEventListener likeEventListener;

    @Test
    void onMessage() {
        LikeEventDto likeEventDto = new LikeEventDto();
        Mockito.when(message.toString()).thenReturn("s");
        Mockito.when(jsonMapper.toObject(Mockito.anyString(), Mockito.eq(LikeEventDto.class)))
                .thenReturn(Optional.of(likeEventDto));

        likeEventListener.onMessage(message, "".getBytes());

        Mockito.verify(analyticsEventService, Mockito.times(1))
                .likeEventSave(likeEventDto);
    }
}