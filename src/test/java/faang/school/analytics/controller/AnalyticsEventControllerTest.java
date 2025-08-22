package faang.school.analytics.controller;


import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.dto.RecommendationFilterDto;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.AnalyticsEvent;
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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource("faang.school.analytics.controller.AnalyticsEventControllerData#invalidFilterDto")
    @DisplayName("")
    public void getByFilter_WhenInvalidFilterDto(TimeIntervalType intervalType,
                                                 LocalDateTime start,
                                                 LocalDateTime end,
                                                 String expectedError) {
        RecommendationFilterDto filterDto = new RecommendationFilterDto(
                1L,
                EventType.RECOMMENDATION_RECEIVED,
                intervalType,
                start,
                end
        );

        DataValidationException exception = assertThrows(DataValidationException.class, filterDto::validate);
        assertEquals(expectedError, exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("faang.school.analytics.controller.AnalyticsEventControllerData#validFilterDto")
    @DisplayName("Validation should pass for valid filter combinations")
    void getByFilter_WhenValidFilterDto(TimeIntervalType intervalType, LocalDateTime start, LocalDateTime end) {
        RecommendationFilterDto filterDto = new RecommendationFilterDto(
                1L,
                EventType.RECOMMENDATION_RECEIVED,
                intervalType,
                start,
                end
        );

        assertDoesNotThrow(filterDto::validate);
    }

    @Test
    @DisplayName("")
    public void getByFilter_() throws Exception {
        RecommendationFilterDto filterDto = new RecommendationFilterDto(
                2L,
                EventType.RECOMMENDATION_RECEIVED,
                null,
                null,
                null
        );
        AnalyticsEvent event = new AnalyticsEvent(
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
                .andExpect(jsonPath("$[0].receiverId").value(2))
                .andExpect(jsonPath("$[0].actorId").value(3))
                .andExpect(jsonPath("$[0].eventType").value("RECOMMENDATION_RECEIVED")
                );
    }


}