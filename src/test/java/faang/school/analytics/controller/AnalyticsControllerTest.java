package faang.school.analytics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.config.context.UserContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyticsController.class)
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalyticsEventService analyticsEventService;

    @MockBean
    private UserContext userContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void saveEvent() throws Exception {
        AnalyticsEventDto analyticsEventDto = AnalyticsEventDto.builder()
                .id(1L)
                .eventType(EventType.PROFILE_VIEW)
                .actorId(1L)
                .receiverId(1L)
                .receivedAt(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/analytics/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-user-id", "1")
                        .content(objectMapper.writeValueAsString(analyticsEventDto)))
                .andExpect(status().isOk());

        verify(analyticsEventService, times(1)).saveEvent(any(AnalyticsEventDto.class));
    }

    @Test
    void getAnalyticsWithFilters() throws Exception {
        AnalyticsFilterDto analyticsFilterDto = AnalyticsFilterDto.builder()
                .receiverId(1L)
                .eventType(EventType.PROFILE_VIEW)
                .from(LocalDateTime.now().minusDays(1))
                .to(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/analytics/filters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-user-id", "1")
                        .content(objectMapper.writeValueAsString(analyticsFilterDto)))
                .andExpect(status().isOk());

        verify(analyticsEventService, times(1)).getAnalytics(any(AnalyticsFilterDto.class));
    }
}
