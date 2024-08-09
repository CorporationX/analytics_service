package faang.school.analytics.controller;

import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static faang.school.analytics.util.TestDataFactory.END_DATE;
import static faang.school.analytics.util.TestDataFactory.ID;
import static faang.school.analytics.util.TestDataFactory.START_DATE;
import static faang.school.analytics.util.TestDataFactory.createAnalyticsEventDto;
import static faang.school.analytics.util.TestDataFactory.createEventType;
import static faang.school.analytics.util.TestDataFactory.createInterval;
import static java.util.List.of;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AnalyticsControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    private AnalyticsController analyticsController;
    @Mock
    private AnalyticsEventService analyticsEventService;

    @BeforeEach
    void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(analyticsController).build();
    }

    @Test
    void shouldReturnAnalyticsEventsWhenAllParametersAreGiven() throws Exception {
        // given - precondition
        var eventDtoList = of(createAnalyticsEventDto());
        var eventType = createEventType();
        var interval = createInterval();

        when(analyticsEventService.getAnalytics(ID, eventType, interval, START_DATE, END_DATE))
                .thenReturn(eventDtoList);

        // when - action
        var response = mockMvc.perform(get("/analytics")
                .param("id", ID.toString())
                .param("eventType", eventType.name())
                .param("interval", interval.name())
                .param("start", START_DATE.toString())
                .param("end", END_DATE.toString()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(eventDtoList.size())))
                .andExpect(jsonPath("$[0].id").value(eventDtoList.get(0).id()))
                .andDo(print());
    }

    @Test
    void shouldReturnAnalyticsEventsWhenOnlyIntervalIsGiven() throws Exception {
        // given - precondition
        var eventDtoList = of(createAnalyticsEventDto());
        var eventType = createEventType();
        var interval = createInterval();

        when(analyticsEventService.getAnalytics(ID, eventType, interval, null,null))
                .thenReturn(eventDtoList);

        // when - action
        var response = mockMvc.perform(get("/analytics")
                .param("id", ID.toString())
                .param("eventType", eventType.name())
                .param("interval", interval.name()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(eventDtoList.size())))
                .andExpect(jsonPath("$[0].id").value(eventDtoList.get(0).id()))
                .andDo(print());
    }

    @Test
    void shouldReturnAnalyticsEventsWhenOnlyStartAndEndDatesAreGiven() throws Exception {
        // given - precondition
        var eventDtoList = of(createAnalyticsEventDto());
        var eventType = createEventType();

        when(analyticsEventService.getAnalytics(ID, eventType, null, START_DATE, END_DATE))
                .thenReturn(eventDtoList);

        // when - action
        var response = mockMvc.perform(get("/analytics")
                .param("id", ID.toString())
                .param("eventType", eventType.name())
                .param("start", START_DATE.toString())
                .param("end", END_DATE.toString()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(eventDtoList.size())))
                .andExpect(jsonPath("$[0].id").value(eventDtoList.get(0).id()))
                .andDo(print());
    }
}