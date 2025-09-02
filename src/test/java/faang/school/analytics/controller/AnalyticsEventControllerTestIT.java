package faang.school.analytics.controller;

import com.redis.testcontainers.RedisContainer;
import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционный тест для контроллера управления анатиликой событий
 *
 * @author Linempy
 * @since 02.09.2025
 */
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class AnalyticsEventControllerTestIT {

    @Container
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:13.6");

    @Container
    private static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis/redis-stack:latest"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;

    @Autowired
    private UserContext context;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);

        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
    }

    @BeforeEach
    void setUp() {
        analyticsEventRepository.deleteAll();
    }

    @Test
    void shouldReturnAnalyticsByUserId() throws Exception {
        AnalyticsEvent event1 = new AnalyticsEvent(1L, 2L, 1L, EventType.RECOMMENDATION_RECEIVED, null);
        analyticsEventRepository.save(event1);

        AnalyticsEvent event2 = new AnalyticsEvent(2L, 3L, 1L, EventType.POST_PUBLISHED, null);
        analyticsEventRepository.save(event2);

        mockMvc.perform(get("/analytics?id=3&eventType=POST_PUBLISHED")
                        .header("x-user-id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].receiverId").value(3));
    }

    @Test
    void shouldReturnEmptyListForNonExistingUser() throws Exception {
        mockMvc.perform(get("/analytics?id=999&eventType=POST_PUBLISHED")
                        .header("x-user-id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}