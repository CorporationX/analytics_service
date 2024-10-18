package faang.school.analytics.controller;

import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import faang.school.analytics.validator.AnalyticControllerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalyticsController.class)
public class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserContext userContext;

    @MockBean
    private AnalyticsEventServiceImpl analyticsEventServiceImpl;

    @MockBean
    private AnalyticControllerValidator analyticControllerValidator;

    private Long id;
    private String interval;
    @BeforeEach
    public void setup() {
        id = 1L;
        interval = "WEEK";
    }

    @Test
    void testGetAnalytics_withValidParams() throws Exception {
        String eventType = "FOLLOWER";
        AnalyticsEventDto dto = new AnalyticsEventDto(1L, 2L, 3L, EventType.FOLLOWER, LocalDateTime.now());
        doNothing().when(analyticControllerValidator)
                .validateIntervalAndDates(anyString(), anyString(), anyString());

        when(analyticsEventServiceImpl.getAnalytics(
                anyLong(), any(), any(), any(), any())
        ).thenReturn(Collections.singletonList(dto));

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

    @Test
    void testGetAnalytics_withMissingParams() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/analytics")
                        .header("x-user-id", 8L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAnalytics_withInvalidEventType() throws Exception {
        String eventType = "INVALID_EVENT_TYPE";
        mockMvc.perform(MockMvcRequestBuilders.get("/analytics")
                        .header("x-user-id", 8L)
                        .param("id", id.toString())
                        .param("eventType", eventType)
                        .param("interval", interval))
                .andExpect(status().isBadRequest());
    }
}




