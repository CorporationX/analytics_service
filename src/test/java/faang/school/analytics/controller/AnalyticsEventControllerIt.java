package faang.school.analytics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.testcontainers.RedisContainer;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class AnalyticsEventControllerIt {

    @Container
    public static PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:13.6");
    @Container
    private static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis/redis-stack:latest"));

    private static final String URL_GETTING_ANALYTICS = "/analytics/filter";
    private static final String USER_PARAMETER_ID_NAME = "x-user-id";

    private final Long userId = 5L;
    private final EventType eventType = EventType.GOAL_COMPLETED;
    private final LocalDateTime date = LocalDateTime.of(2020, 1, 1, 0, 0);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AnalyticsEventRepository analyticsRepository;

    @AfterEach
    void tearDown() {
        analyticsRepository.deleteAll();
    }

    @Test
    void testNegativeGetAnalyticsWhenRequestBodyIsNull() throws Exception {
        mockMvc.perform(post(URL_GETTING_ANALYTICS)
                        .header(USER_PARAMETER_ID_NAME, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testNegativeGetAnalyticsWhenFilterIsIncorrect() throws Exception {
        AnalyticsEventFilterDto analytics = createAnalyticsFilter(userId, null);

        mockMvc.perform(post(URL_GETTING_ANALYTICS)
                        .header(USER_PARAMETER_ID_NAME, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(analytics)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPositiveGetAnalyticsWhenReturnZeroElements() throws Exception {
        AnalyticsEventFilterDto analytics = createAnalyticsFilter(userId, eventType);

        mockMvc.perform(post(URL_GETTING_ANALYTICS)
                        .header(USER_PARAMETER_ID_NAME, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(analytics)))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testPositiveGetAnalyticsWhenReturnElements() throws Exception {
        AnalyticsEventFilterDto analytics = createAnalyticsFilter(userId, eventType);
        analyticsRepository.save(createEvent(userId, eventType));
        List<AnalyticsEventDto> result = List.of(createAnalyticsDto(userId, eventType));

        mockMvc.perform(post(URL_GETTING_ANALYTICS)
                        .header(USER_PARAMETER_ID_NAME, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(analytics)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)));
    }

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        POSTGRESQL_CONTAINER.start();
        REDIS_CONTAINER.start();

        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);

        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private AnalyticsEventFilterDto createAnalyticsFilter(Long id, EventType type) {
        return AnalyticsEventFilterDto.builder()
                .receiverId(id)
                .eventType(type)
                .build();
    }

    private AnalyticsEvent createEvent(Long id, EventType type) {
        return AnalyticsEvent.builder()
                .receiverId(id)
                .actorId(userId)
                .eventType(type)
                .receivedAt(date)
                .build();
    }

    private AnalyticsEventDto createAnalyticsDto(Long id, EventType type) {
        return AnalyticsEventDto.builder()
                .receiverId(id)
                .actorId(userId)
                .eventType(type)
                .receivedAt(date)
                .build();
    }

}
