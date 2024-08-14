package faang.school.analytics.controller;

import faang.school.analytics.service.AnalyticsService;
import faang.school.analytics.validator.AnalyticsEventValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AnalyticsControllerTest {

    @Mock
    private AnalyticsService analyticsService;

    @Mock
    private AnalyticsEventValidator analyticsEventValidator;

    @InjectMocks
    private AnalyticsController analyticsController;

    private MockMvc mockMvc;
    private long eventId;
    private int eventTypeOrdinal;
    private String intervalStr;
    private String fromStr;
    private String toStr;

    @BeforeEach
    public void setUp() {
        eventId = 1L;
        eventTypeOrdinal = 2;
        intervalStr = "DAY";
        fromStr = "fromStr";
        toStr = "toStr";
        mockMvc = MockMvcBuilders.standaloneSetup(analyticsController).build();
    }

    @Test
    @DisplayName("testing getAnalytics method")
    void getAnalytics() throws Exception {
        mockMvc.perform(get("/api/v1/analytics")
                        .param("eventId", String.valueOf(eventId))
                        .param("eventTypeOrdinal", String.valueOf(eventTypeOrdinal))
                        .param("intervalStr", intervalStr)
                        .param("fromStr", fromStr)
                        .param("toStr", toStr))
                .andExpect(status().isOk());
        verify(analyticsEventValidator, times(1))
                .validateTimeBoundsPresence(intervalStr, fromStr, toStr);
        verify(analyticsService, times(1))
                .getAnalytics(eventId, eventTypeOrdinal, intervalStr, fromStr, toStr);
    }
}