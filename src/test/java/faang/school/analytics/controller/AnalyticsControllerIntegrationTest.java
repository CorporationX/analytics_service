package faang.school.analytics.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.context.UserContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class AnalyticsControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserContext userContext;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("testdb")
                    .withUsername("admin")
                    .withPassword("admin")
                    .withInitScript("schema_for_AnalyticController.sql");

    @Container
    public static GenericContainer<?> redisContainer = new GenericContainer<>("redis:latest")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void overrideSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", postgresContainer::getDriverClassName);
        registry.add("spring.liquibase.enabled", () -> false);
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);
    }

    @Test
    void testGetAnalytics_withValidParams() throws Exception {
        String startDate = "2024-10-09T16:37:34.252454600";
        String endDate = "2024-10-11T16:37:34.252454600";
        Long id = 3L;
        String eventType = "PROFILE_APPEARED_IN_SEARCH";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/analytics")
                        .header("x-user-id", 8L)
                        .param("id", id.toString())
                        .param("eventType", eventType)
                        .param("startDate", startDate)
                        .param("endDate", endDate))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].eventType").value(eventType))
                .andExpect(jsonPath("$[0].receiverId").value(id))
                .andExpect(jsonPath("$[1].eventType").value(eventType))
                .andExpect(jsonPath("$[1].receiverId").value(id))
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootArray = mapper.readTree(jsonResponse);
        Iterator<JsonNode> elements = rootArray.elements();
        List<LocalDateTime> localDateTimes = new ArrayList<>();
        while (elements.hasNext()) {
            JsonNode element = elements.next();
            JsonNode receivedAtNode = element.get("receivedAt");
            LocalDateTime receivedAt = LocalDateTime.of(
                    receivedAtNode.get(0).asInt(),
                    receivedAtNode.get(1).asInt(),
                    receivedAtNode.get(2).asInt(),
                    receivedAtNode.get(3).asInt(),
                    receivedAtNode.get(4).asInt(),
                    receivedAtNode.get(5).asInt(),
                    receivedAtNode.get(6).asInt()
            );
            localDateTimes.add(receivedAt);
        }

        assertAll(
                () -> assertTrue(localDateTimes.get(0).isAfter(LocalDateTime.parse(startDate))),
                () -> assertTrue(localDateTimes.get(0).isBefore(LocalDateTime.parse(endDate))),
                () -> assertTrue(localDateTimes.get(1).isAfter(LocalDateTime.parse(startDate))),
                () -> assertTrue(localDateTimes.get(1).isBefore(LocalDateTime.parse(endDate)))
        );
    }
}