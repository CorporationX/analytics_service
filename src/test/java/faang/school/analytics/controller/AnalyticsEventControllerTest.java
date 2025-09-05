package faang.school.analytics.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.dto.AnalyticsViewDto;
import faang.school.analytics.dto.RecommendationFilterDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.TimeIntervalType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(AnalyticsEventController.class)
class AnalyticsEventControllerTest {

    @MockBean
    private AnalyticsEventService service;

    @MockBean
    private UserContext userContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource("faang.school.analytics.controller.AnalyticsEventControllerData#invalidTimeTypeFilterDto")
    @DisplayName("Проверка метода с невалидными параметрами типами времени")
    void shouldReturnBadRequestWhenDtoInvalidTimeType(TimeIntervalType intervalType,
                                              LocalDateTime start,
                                              LocalDateTime end) throws Exception {
        RecommendationFilterDto filterDto = new RecommendationFilterDto(
                1L,
                EventType.RECOMMENDATION_RECEIVED,
                intervalType,
                start,
                end
        );

        assertFalse(filterDto::isTimeTypeConsistent);
    }

    @ParameterizedTest
    @MethodSource("faang.school.analytics.controller.AnalyticsEventControllerData#invalidTimeRangeFilterDto")
    @DisplayName("Проверка метода с невалидными параметрами промежутка времени")
    void shouldReturnBadRequestWhenDtoInvalidTimeRange(TimeIntervalType intervalType,
                                              LocalDateTime start,
                                              LocalDateTime end) throws Exception {
        RecommendationFilterDto filterDto = new RecommendationFilterDto(
                1L,
                EventType.RECOMMENDATION_RECEIVED,
                intervalType,
                start,
                end
        );

        assertFalse(filterDto::isTimeRangeValid);
    }

    @ParameterizedTest
    @MethodSource("faang.school.analytics.controller.AnalyticsEventControllerData#validFilterDto")
    @DisplayName("Успешная проверка на валидных данных")
    void getByFilter_WhenValidFilterDto(TimeIntervalType intervalType, LocalDateTime start, LocalDateTime end) {
        RecommendationFilterDto filterDto = new RecommendationFilterDto(
                1L,
                EventType.RECOMMENDATION_RECEIVED,
                intervalType,
                start,
                end
        );

        assertTrue(filterDto::isTimeRangeValid);
        assertTrue(filterDto::isTimeTypeConsistent);
    }

    @Test
    @DisplayName("GET /analytics - возвращает корректный результат")
    public void getByFilterSuccess() throws Exception {
        RecommendationFilterDto filterDto = new RecommendationFilterDto(
                2L,
                EventType.RECOMMENDATION_RECEIVED,
                null,
                null,
                null
        );
        AnalyticsViewDto event = new AnalyticsViewDto(
                1L,
                2L,
                3L,
                EventType.RECOMMENDATION_RECEIVED,
                LocalDateTime.now()
        );

        when(service.getAnalytics(filterDto)).thenReturn(List.of(event));

        mockMvc.perform(get("/analytics")
                        .header("x-user-id", "123")
                        .param("id", "2")
                        .param("eventType", "RECOMMENDATION_RECEIVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].receiverId").value(3))
                .andExpect(jsonPath("$[0].actorId").value(2))
                .andExpect(jsonPath("$[0].eventType").value("RECOMMENDATION_RECEIVED")
                );
    }
}