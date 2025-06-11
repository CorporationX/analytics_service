package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private AnalyticsEventController analyticsEventController;

    private AnalyticsEventDto eventDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(analyticsEventController).build();
        analyticsEventController = new AnalyticsEventController(analyticsEventService);
        eventDto = new AnalyticsEventDto(1L, 1L, 2L, "POST_PUBLISHED", LocalDateTime.now());
    }

    @Test
    void testGetAnalytics_WithValidInterval() throws Exception {
        when(analyticsEventService.getAnalytics(anyLong(), any(), any(), any(), any()))
                .thenReturn(List.of(eventDto));

        mockMvc.perform(get("/api/v1/analytics")
                        .param("receiverId", "1")
                        .param("eventType", "POST_PUBLISHED")
                        .param("interval", "DAY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventType").value("POST_PUBLISHED"));

        verify(analyticsEventService, times(1))
                .getAnalytics(1L, EventType.POST_PUBLISHED, Interval.DAY, null, null);
    }

    @Test
    void testGetAnalytics_WithInvalidInterval() throws Exception {
        mockMvc.perform(get("/api/v1/analytics")
                        .param("receiverId", "1")
                        .param("eventType", "POST_PUBLISHED")
                        .param("interval", "INVALID_INTERVAL"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAnalytics_WithoutInterval() throws Exception {
        when(analyticsEventService.getAnalytics(anyLong(), any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(eventDto));

        mockMvc.perform(get("/api/v1/analytics")
                        .param("receiverId", "1")
                        .param("eventType", "POST_PUBLISHED")
                        .param("start", "2025-06-11 00:00")
                        .param("end", "2025-06-12 00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventType").value("POST_PUBLISHED"));

        verify(analyticsEventService, times(1)).getAnalytics(1L, EventType.POST_PUBLISHED, null,
                LocalDateTime.parse("2025-06-11T00:00"), LocalDateTime.parse("2025-06-12T00:00"));
    }

    @Test
    void testGetAnalytics_WithoutStartAndEnd() throws Exception {
        mockMvc.perform(get("/api/v1/analytics")
                        .param("receiverId", "1")
                        .param("eventType", "POST_PUBLISHED"))
                .andExpect(status().isBadRequest());
    }

}