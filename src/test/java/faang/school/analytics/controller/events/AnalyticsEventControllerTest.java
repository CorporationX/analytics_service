package faang.school.analytics.controller.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.controller.advices.DataValidationControllerAdvice;
import faang.school.analytics.domain.dto.events.analytic.AnalyticsEventDto;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.service.events.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {AnalyticsEventController.class, DataValidationControllerAdvice.class, UserContext.class})
class AnalyticsEventControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AnalyticsEventService analyticsEventService;

    ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void testSaveEventBadRequest() throws Exception {
        mockMvc.perform(post("/analytics/events"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSaveEventOk() throws Exception {
        AnalyticsEventDto dto = AnalyticsEventDto.builder()
                .actorId(1L)
                .receiverId(1L)
                .eventTypeNumber(1)
                .build();

        AnalyticsEventDto dtoAfterSave = AnalyticsEventDto.builder()
                .id(1L)
                .actorId(1L)
                .receiverId(1L)
                .eventTypeNumber(1)
                .receivedAt(LocalDateTime.now())
                .build();
        Mockito.when(analyticsEventService.saveEvent(any())).thenReturn(dtoAfterSave);

        String rq = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post("/analytics/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rq))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.actorId").value(1))
                .andExpect(jsonPath("$.receiverId").value(1))
                .andExpect(jsonPath("$.eventTypeNumber").value(1))
                .andExpect(jsonPath("$.receivedAt").exists());
    }

    @Test
    void testSaveEventValidationException() throws Exception {
        AnalyticsEventDto dto = AnalyticsEventDto.builder()
                .id(1L)
                .actorId(1L)
                .receiverId(1L)
                .eventTypeNumber(1)
                .build();

        Mockito.doThrow(new DataValidationException("Error message")).when(analyticsEventService).saveEvent(any());

        String rq = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post("/analytics/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rq))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Error message"))
                .andExpect(jsonPath("$.dateTime").exists());

    }

    @Test
    void testGetEventsBadRequest() throws Exception {
        mockMvc.perform(get("/analytics/events"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetEventsOk() throws Exception {
        AnalyticsEventDto event = AnalyticsEventDto.builder()
                .id(1L)
                .actorId(1L)
                .receiverId(1L)
                .eventTypeNumber(1)
                .receivedAt(LocalDateTime.now())
                .build();
        Mockito.when(analyticsEventService.getAnalytics(any())).thenReturn(List.of(event));

        mockMvc.perform(get("/analytics/events")
                        .param("receiverId", "1")
                        .param("eventType", "PROFILE_VIEW"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].actorId").value(1))
                .andExpect(jsonPath("$[0].receiverId").value(1))
                .andExpect(jsonPath("$[0].eventTypeNumber").value(1))
                .andExpect(jsonPath("$[0].receivedAt").exists());
    }

    @Test
    void testGetEventsValidationException() throws Exception {
        Mockito.doThrow(new DataValidationException("Error message")).when(analyticsEventService).getAnalytics(any());

        mockMvc.perform(get("/analytics/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("receiverId", "1")
                        .param("eventType", "PROFILE_VIEW"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Error message"))
                .andExpect(jsonPath("$.dateTime").exists());

    }

}
