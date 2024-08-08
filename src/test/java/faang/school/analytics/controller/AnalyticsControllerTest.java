package faang.school.analytics.controller;

import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
public class AnalyticsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Spy
    @InjectMocks
    private AnalyticsController analyticsController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(analyticsController).build();
    }

    @Test
    public void testValidRequestWithEventTypeString() throws Exception {
        when(analyticsEventService.getAnalytics(1L, EventType.PROFILE_VIEW, Interval.DAY, null, null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/analytics")
                        .header("x-user-id", 1)
                        .param("receiverId", "1")
                        .param("eventType", "PROFILE_VIEW")
                        .param("interval", "DAY"))
                .andExpect(status().isOk());
    }

    @Test
    public void testValidRequestWithEventTypeNumber() throws Exception {
        when(analyticsEventService.getAnalytics(1L, EventType.PROFILE_VIEW, Interval.DAY, null, null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/analytics")
                        .header("x-user-id", 1)
                        .param("receiverId", "1")
                        .param("eventType", "0")
                        .param("interval", "3"))
                .andExpect(status().isOk());
    }
}