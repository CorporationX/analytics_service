package faang.school.analytics.integration.controller.analyticsevent;

import faang.school.analytics.controller.analyticsevent.AnalyticsEventController;
import faang.school.analytics.integration.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AnalyticsEventControllerIT extends IntegrationTestBase {
    private final AnalyticsEventController analyticsEventController;
    private MockMvc mockMvc;

    @Autowired
    AnalyticsEventControllerIT(AnalyticsEventController analyticsEventController) {
        this.analyticsEventController = analyticsEventController;
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(analyticsEventController).build();
    }

    @Test
    @DisplayName("Get Analytics Events IT with Not Null Interval")
    void testGetAnalyticsEventsWithNotNullInterval() throws Exception {
        mockMvc.perform(get("/api/v1/analytics-events")
                        .param("receiver-id", "2")
                        .param("event-type", "PROFILE_VIEW")
                        .param("interval", "WEEK")
                        .param("from", "2023-12-01T00:00:00.000")
                        .param("to", "2024-12-01T00:00:00.000"))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.length()").value(1),
                        jsonPath("$[0].id").value(3),
                        jsonPath("$[0].receiverId").value(2),
                        jsonPath("$[0].actorId").value(1),
                        jsonPath("$[0].eventType").value("PROFILE_VIEW")
                );
    }

    @Test
    @DisplayName("Get Analytics Events IT with Null Interval")
    void testGetAnalyticsEventsWithNullInterval() throws Exception {
        mockMvc.perform(get("/api/v1/analytics-events")
                        .param("receiver-id", "1")
                        .param("event-type", "PROFILE_VIEW")
                        .param("from", "2023-12-01T00:00:00.000")
                        .param("to", "2024-12-01T00:00:00.000"))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.length()").value(2),
                        jsonPath("$[0].id").value(1),
                        jsonPath("$[0].receiverId").value(1),
                        jsonPath("$[0].actorId").value(2),
                        jsonPath("$[0].eventType").value("PROFILE_VIEW"),
                        jsonPath("$[1].id").value(2),
                        jsonPath("$[1].receiverId").value(1),
                        jsonPath("$[1].actorId").value(3),
                        jsonPath("$[1].eventType").value("PROFILE_VIEW")
                );
    }
}