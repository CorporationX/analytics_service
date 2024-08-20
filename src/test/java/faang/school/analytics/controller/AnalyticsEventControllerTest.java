package faang.school.analytics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.dto.AnalyticInfoDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Testcontainers
@Sql(scripts = {"classpath:db/schema.sql", "classpath:db/data.sql"})
@Sql(scripts = "classpath:db/dropTable.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AnalyticsEventControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withExposedPorts(5432);

    @Container
    public static GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.0.5-alpine")
            .withExposedPorts(6379);

    @BeforeAll
    public static void setup() {
        System.setProperty("DB_URL", postgreSQLContainer.getJdbcUrl());
        System.setProperty("DB_USERNAME", postgreSQLContainer.getUsername());
        System.setProperty("DB_PASSWORD", postgreSQLContainer.getPassword());

        System.setProperty("spring.data.redis.host", redisContainer.getHost());
        System.setProperty("spring.data.redis.port", redisContainer.getMappedPort(6379).toString());
    }

    @Test
    @DisplayName("Get analytics events by date range")
    public void testGetAnalyticsEvents() throws Exception {

        long receiverId = 1;
        String eventType = "USER_FOLLOWER";
        LocalDateTime from = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 1, 7, 0, 0);

        AnalyticInfoDto analyticInfoDto = AnalyticInfoDto.builder()
                .receiverId(receiverId)
                .eventType(EventType.USER_FOLLOWER)
                .interval(null)
                .from(from)
                .to(to)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-user-id", "1");

        mockMvc.perform(post("/analytics")
                        .content(objectMapper.writeValueAsString(analyticInfoDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].eventType").value(eventType))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].receivedAt").value("2024-01-03T00:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].actorId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].receiverId").value(1))

                .andExpect(MockMvcResultMatchers.jsonPath("$[1].actorId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].receiverId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].eventType").value(eventType))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].receivedAt").value("2024-01-02T00:00:00"))


                .andExpect(MockMvcResultMatchers.jsonPath("$[2].actorId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].receiverId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].eventType").value(eventType))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].receivedAt").value("2024-01-01T00:01:00"))

                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get analytics events by interval")
    public void testGetAnalyticsEventsByInterval() throws Exception {

        long receiverId = 1;
        String eventType = "POST_LIKE";
        LocalDateTime from = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 1, 7, 0, 0);

        AnalyticInfoDto analyticInfoDto = AnalyticInfoDto.builder()
                .receiverId(receiverId)
                .eventType(EventType.POST_LIKE)
                .interval(Interval.DAY)
                .from(from)
                .to(to)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-user-id", "1");

        mockMvc.perform(post("/analytics")
                        .content(objectMapper.writeValueAsString(analyticInfoDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].eventType").value(eventType))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].actorId").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].receiverId").value(1))
                .andExpect(status().isOk());
    }
}
