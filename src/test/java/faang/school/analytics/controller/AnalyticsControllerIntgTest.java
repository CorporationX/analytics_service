package faang.school.analytics.controller;

import faang.school.analytics.config.context.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class AnalyticsControllerIntgTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserContext userContext;

    private JdbcTemplate jdbc;
    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("testdb")
                    .withUsername("admin")
                    .withPassword("admin")
                    .withInitScript("schema_for_AnalyticController.sql");

    @BeforeEach
    public void setUp() {
        DataSource dataSource = new DriverManagerDataSource(postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(), postgresContainer.getPassword());
        jdbc = new JdbcTemplate(dataSource);
    }


    @Test
    void testGetAnalytics_withValidParams() throws Exception {
        String interval = "WEEK";
        Long id = 100L;
        String eventType = "FOLLOWER";
        mockMvc.perform(MockMvcRequestBuilders.get("/analytics")
                        .header("x-user-id", 8L)
                        .param("id", id.toString())
                        .param("eventType", eventType)
                        .param("interval", interval))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray());
    }
}