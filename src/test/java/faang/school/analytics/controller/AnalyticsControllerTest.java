package faang.school.analytics.controller;

import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import faang.school.analytics.validator.AnalyticControllerValidator;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    void testGetAnalytics_withValidParams() throws Exception {
        Long id = 1L;
        String eventType = "FOLLOWER";
        String interval = "WEEK";

        // Мокируем поведение валидатора
        doNothing().when(analyticControllerValidator).validateIntervalAndDates(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
        );

        // Мокируем результат вызова сервиса
        when(analyticsEventServiceImpl.getAnalytics(
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any())
        ).thenReturn(Collections.singletonList(new AnalyticsEventDto(1L,2L,3L, EventType.FOLLOWER, LocalDateTime.now())));

        // Выполняем запрос и проверяем результат
        mockMvc.perform(MockMvcRequestBuilders.get("/analytics")
                        .header("x-user-id", 8L)
                        .param("id", id.toString())
                        .param("eventType", eventType)
                        .param("interval", interval))
                .andDo(print()) // выводим запрос и ответ в консоль для отладки
                .andExpect(status().isOk()) // ожидаем HTTP статус 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // проверяем, что тип контента JSON
                .andExpect(content().json("[]")); // проверяем, что тело ответа не пустое
    }
}
//    @Test
//    void testGetAnalytics_withValidParams() throws Exception {
//        Long id = 1L;
//        EventType eventType = EventType.FOLLOWER;
//        Interval interval = Interval.WEEK;
//        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
//        LocalDateTime endDate = LocalDateTime.now();
//        AnalyticsEventDto analyticsEventDto = new AnalyticsEventDto(2L, 1L, 5L, eventType, LocalDateTime.now().minusDays(3));
//
//        doNothing().when(analyticControllerValidator).validateIntervalAndDates(anyString(), anyString(), anyString());
//        when(analyticsEventServiceImpl.getAnalytics(id, eventType, interval, startDate, endDate))
//                .thenReturn(Collections.singletonList(analyticsEventDto));
//        System.out.println(analyticsEventDto);
//        mockMvc.perform(MockMvcRequestBuilders.get("/analytics")
//                        .header("x-user-id", 7L)
//                        .param("id", id.toString())
//                        .param("eventType", "FOLLOWER")
//                        .param("interval", "WEEK")
//                        .param("startDate", startDate.toString())
//                        .param("endDate", endDate.toString())
//                )
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(content().contentType("application/json"));
////                .andExpect(jsonPath("$").isArray());
//    }

//    @Test
//    void testGetAnalytics_withMissingParams() throws Exception {
//        // Testing case where required parameters are missing
//        mockMvc.perform(get("/analytics")
//                        .param("eventType", "CLICK"))  // Missing 'id' parameter
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void testGetAnalytics_withInvalidEventType() throws Exception {
//        // Testing case with invalid event type
//        mockMvc.perform(get("/analytics")
//                        .param("id", "1")
//                        .param("eventType", "INVALID_EVENT_TYPE"))
//                .andExpect(status().isBadRequest());
//    }


