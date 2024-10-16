package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.mapper.AnalyticsCommentEventMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class CommentEventListenerTest {

    @Container
    public static GenericContainer<?> redisContainer = new GenericContainer<>("redis:latest")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", () -> redisContainer.getHost());
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
    }

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ChannelTopic commentEventTopic;

    @MockBean
    private AnalyticsCommentEventMapper commentEventMapper;

    @Autowired
    private ObjectMapper objectMapper;

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

        redisTemplate.convertAndSend(commentEventTopic.getTopic(), message);
        Thread.sleep(1000);

        verify(commentEventMapper, times(1)).toAnalyticsEvent(captor.capture());
        assertThat(commentEventDto).usingRecursiveComparison().isEqualTo(captor.getValue());
    }
}

