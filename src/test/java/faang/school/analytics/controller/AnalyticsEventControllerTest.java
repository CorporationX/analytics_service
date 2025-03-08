package faang.school.analytics.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.dto.AnalyticsEventDTO;
import faang.school.analytics.dto.AnalyticsEventRequestDTO;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @InjectMocks
    private AnalyticsEventController analyticsEventController;

    @Mock
    private AnalyticsEventService analyticsEventService;
    private List<AnalyticsEventDTO> listEvents;
    private AnalyticsEventRequestDTO analyticsEventRequestDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(analyticsEventController).build();
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

         analyticsEventRequestDTO = AnalyticsEventRequestDTO.builder()
                .receiverId(2)
                .eventType(EventType.PROFILE_VIEW)
                .from(LocalDateTime.of(2025, 1, 26, 12, 0))
                .to(LocalDateTime.of(2025, 2, 26, 12, 0))
                .build();

        AnalyticsEventDTO analyticsEventDTO = AnalyticsEventDTO.builder()
                .id(1)
                .eventType(EventType.PROFILE_VIEW)
                .receiverId(2)
                .receivedAt(LocalDateTime.of(2025, 2, 20, 10, 0))
                .build();

        listEvents = List.of(analyticsEventDTO);
    }

    @Test
    @DisplayName("The test must return list of analytics by type event, id receiver, time interval")
    void testGetAnalyticsSuccess() throws Exception {
        Mockito.when(analyticsEventService.getAnalytics(analyticsEventRequestDTO))
                .thenReturn(listEvents);

        mockMvc.perform(get("/api/v1/analytics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(analyticsEventRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].eventType").value("PROFILE_VIEW"))
                .andExpect(jsonPath("$[0].receiverId").value(2));

        Mockito.verify(analyticsEventService, Mockito.times(1))
                .getAnalytics(analyticsEventRequestDTO);
    }
}

