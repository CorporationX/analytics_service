package faang.school.analytics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsFilterDto;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsService;

    @InjectMocks
    private AnalyticsEventController analyticsController;
    private List<AnalyticsEventDto> testList;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(analyticsController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        testList = new ArrayList<>();
    }

    @Test
    public void testGetAnalytics_InvalidId() throws Exception {
        mockMvc.perform(get("/analytics/list")
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .param("receiverId", "null")
                        .param("eventType", "PROJECT_VIEW"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAnalytics_ValidEventType() throws Exception {
        mockMvc.perform(get("/analytics/list")
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .param("receiverId", "1")
                        .param("eventType", "null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAnalytics_ValidInterval() throws Exception {
        AnalyticsFilterDto filter = new AnalyticsFilterDto();
        filter.setReceiverId(1L);
        filter.setEventType(EventType.PROJECT_VIEW);
        filter.setInterval(Interval.DAILY);

        AnalyticsEventDto firstUser = new AnalyticsEventDto();
        firstUser.setReceiverId(1L);
        firstUser.setEventType(EventType.PROJECT_VIEW);
        testList = List.of(firstUser);

        when(analyticsService.getAnalytics(any())).thenReturn(testList);

        mockMvc.perform(get("/analytics/list")
                        .content(objectMapper.writeValueAsString(filter))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].receiverId").value(firstUser.getReceiverId()))
                .andExpect(jsonPath("$[0].eventType").value(firstUser.getEventType().toString()));
    }

    @Test
    public void testGetAnalytics_ValidDateRange() throws Exception {
        AnalyticsFilterDto filter = new AnalyticsFilterDto();
        filter.setReceiverId(1L);
        filter.setEventType(EventType.PROJECT_VIEW);
        filter.setFrom(LocalDateTime.now().minusHours(1));
        filter.setTo(LocalDateTime.now().plusHours(2));

        AnalyticsEventDto firstUser = new AnalyticsEventDto();
        firstUser.setReceiverId(1L);
        firstUser.setEventType(EventType.PROJECT_VIEW);
        testList = List.of(firstUser);

        when(analyticsService.getAnalytics(any())).thenReturn(testList);

        mockMvc.perform(get("/analytics/list")
                        .content(objectMapper.writeValueAsString(filter))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].receiverId").value(firstUser.getReceiverId()))
                .andExpect(jsonPath("$[0].eventType").value(firstUser.getEventType().toString()));
    }
}
