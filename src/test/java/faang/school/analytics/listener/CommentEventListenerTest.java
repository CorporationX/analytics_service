package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.mapper.AnalyticsCommentEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.util.BaseContextTest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CommentEventListenerTest extends BaseContextTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ChannelTopic commentEventTopic;

    @MockBean
    private AnalyticsCommentEventMapper commentEventMapper;

    @Captor
    ArgumentCaptor<CommentEventDto> captor;

    @Test
    public void testListenerReceivesMessage() throws JsonProcessingException, InterruptedException {
        CommentEventDto commentEventDto = CommentEventDto.builder()
                .commentId(1L)
                .commentAuthorId(2L)
                .postAuthorId(3L)
                .build();
        String message = objectMapper.writeValueAsString(commentEventDto);

        when(commentEventMapper.toAnalyticsEvent(any(CommentEventDto.class))).thenReturn(new AnalyticsEvent());

        redisTemplate.convertAndSend(commentEventTopic.getTopic(), message);
        Thread.sleep(1000);

        verify(commentEventMapper, times(1)).toAnalyticsEvent(captor.capture());
        assertThat(commentEventDto).usingRecursiveComparison().isEqualTo(captor.getValue());
    }
}

