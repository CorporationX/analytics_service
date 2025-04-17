package faang.school.analytics.controller;

import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyticsController.class)
public class AnalyticsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalyticsService analyticsService;
    @MockBean
    private UserContext userContext;

    private final String x_user_id = "123";
    private final String receiverId = "1";
    private final String eventTypeId = "2";
    private final String intervalId = "0";

    @Test
    @DisplayName("При корректных параметрах возвращается список DTO")
    public void givenValidRequestWithIntervalId_WhenGetAnalytics_ThenReturnOk() throws Exception {
        when(analyticsService.getAnalytics(anyLong(), any(EventType.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(new AnalyticsEventDto()));

        mockMvc.perform(get("/analytics")
                        .header("x-user-id", x_user_id)
                        .param("receiverId", receiverId)
                        .param("eventTypeId", eventTypeId)
                        .param("intervalId", intervalId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Возвращает ошибку при отсутствии параметров дат")
    public void givenRequestWithoutDateParams_WhenGetAnalytics_ThenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/analytics")
                        .header("x-user-id", x_user_id)
                        .param("receiverId", receiverId)
                        .param("eventTypeId", eventTypeId))
                .andExpect(status().isBadRequest());
    }
}
