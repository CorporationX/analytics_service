package faang.school.analytics.messaging;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.service.comment.CommentEventWorker;
import faang.school.analytics.util.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {
    @InjectMocks
    private CommentEventListener commentEventListener;
    @Mock
    private Message message;
    @Mock
    private JsonMapper mapper;
    @Mock
    private CommentEventWorker commentEventWorker;
    private CommentEventDto commentEventDto;

    @BeforeEach
    void setUp() {
        commentEventDto = new CommentEventDto();
    }

    @Test
    void onMessage() {
        Mockito.when(message.toString()).thenReturn("s");
        Mockito.when(mapper.toObject(Mockito.anyString(), Mockito.eq(CommentEventDto.class)))
                .thenReturn(Optional.of(commentEventDto));

        commentEventListener.onMessage(message, "".getBytes());

        Mockito.verify(commentEventWorker, Mockito.times(1))
                .saveFollowEvent(commentEventDto);
    }
}