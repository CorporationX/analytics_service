package faang.school.analytics.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.testcontainers.RedisContainer;
import faang.school.analytics.dto.AnalyticsCreateEventDto;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.EventTypeDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class AnalyticsControllerMockMvcTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;

    @Container
    public static PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:13.6");

    @Container
    public static final RedisContainer REDIS_CONTAINER = new RedisContainer(
            DockerImageName.parse("redis/redis-stack:latest"));

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);

        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
    }

    @Test
    public void testSaveEventWithEmptyBody() throws Exception {
        mockMvc.perform(get("/api/v1/analytics")
                .header("x-user-id", 10)
                .content("{}")).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void testSaveEventPositive() throws Exception {
        AnalyticsCreateEventDto content = new AnalyticsCreateEventDto(
                1L,
                2L,
                LocalDateTime.of(2024, 12, 17, 0, 0, 0),
                EventTypeDto.POST_LIKE
        );
        String json = objectMapper.writeValueAsString(content);

        MvcResult result = mockMvc.perform(post("/api/v1/analytics")
                        .header("x-user-id", 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        AnalyticsEvent analyticsEvent = analyticsEventRepository.findByReceiverIdAndEventType(content.receiverId(),
                        content.eventType().toEventType())
                .findFirst().orElse(null);

        AnalyticsEventDto eventDto = objectMapper.readValue(result.getResponse().getContentAsString(), AnalyticsEventDto.class);

        assertThat(eventDto.eventType()).isEqualTo(EventTypeDto.POST_LIKE);
        assertThat(analyticsEvent).isNotEqualTo(null);
        assertThat(analyticsEvent.getReceiverId()).isEqualTo(content.receiverId());
    }
}