package faang.school.analytics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventResponseDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventControllerTest {

    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private AnalyticsEventController analyticsEventController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private AnalyticsEventResponseDto responseDto;
    private Long receiverId;
    private EventType eventType;
    private Interval interval;
    private LocalDateTime from;
    private LocalDateTime to;
    private Long id;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(analyticsEventController).build();
        objectMapper = new ObjectMapper();
        id = 1L;
        receiverId = 2L;

        responseDto = AnalyticsEventResponseDto.builder()
                .id(id)
                .receiverId(receiverId)
                .eventType(EventType.FOLLOWER)
                .build();
        eventType = EventType.FOLLOWER;
        interval = null;
        from = LocalDateTime.now().minusDays(7);
        to = LocalDateTime.now();
    }

    @Test
    void testGetAnalyticsSuccess() throws Exception {
        when(analyticsEventService.getAnalytics(receiverId, eventType, interval, from, to))
                .thenReturn(List.of(responseDto));

        mockMvc.perform(get("/analytics-events/receiverId/{receiverId}/event-type/{eventType}", receiverId, eventType)
                        .param("interval", interval != null ? interval.toString() : null)
                        .param("from", from.toString())
                        .param("to", to.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responseDto.getId()))
                .andExpect(jsonPath("$[0].receiverId").value(responseDto.getReceiverId()))
                .andExpect(jsonPath("$[0].eventType").value(responseDto.getEventType().name()));

        verify(analyticsEventService, times(1)).getAnalytics(receiverId, eventType, interval, from, to);
    }

    @Test
    void testGetAnalyticsNoResults() throws Exception {
        when(analyticsEventService.getAnalytics(receiverId, eventType, interval, from, to))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/analytics-events/receiverId/{receiverId}/event-type/{eventType}", receiverId, eventType)
                        .param("interval", interval != null ? interval.toString() : null)
                        .param("from", from.toString())
                        .param("to", to.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(analyticsEventService, times(1)).getAnalytics(receiverId, eventType, interval, from, to);
    }
}