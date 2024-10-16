package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.dto.PostViewEvent;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class PostViewEventListenerIntegrationTest {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AnalyticsEventMapper mapper;

    @Autowired
    private AnalyticsEventServiceImpl analyticsEventService;

    @Autowired
    private AnalyticsEventRepository repository;

    private static GenericContainer<?> redisContainer;

    @BeforeAll
    static void setUpContainer() {
        redisContainer = new GenericContainer<>("redis:latest")
                .withExposedPorts(6379);
        redisContainer.start();

        System.setProperty("spring.redis.host", redisContainer.getHost());
        System.setProperty("spring.redis.port", redisContainer.getMappedPort(6379).toString());
    }

    @Test
    void testOnMessageIntegration() throws Exception {
        // Arrange
        PostViewEvent postViewEvent = new PostViewEvent();  // Задай поля для PostViewEvent
        String messagePayload = objectMapper.writeValueAsString(postViewEvent);

        // Посылаем сообщение в Redis, как будто оно пришло
        redisTemplate.convertAndSend("post_view_event_channel", messagePayload);

        // Act
        Thread.sleep(1000);  // Ждем, пока обработается сообщение

        // Assert
        // Проверяем, что событие сохранилось в базе данных
//        AnalyticsEvent savedEntity = repository.findFirstByOrderByIdDesc();
        AnalyticsEvent savedEntity = repository.findById(1L).orElseThrow();
        assertThat(savedEntity).isNotNull();
//        assertThat(savedEntity.getEventType()).isEqualTo("PostViewEvent");
        // Здесь можно проверить и другие поля

        // TODO
    }
}