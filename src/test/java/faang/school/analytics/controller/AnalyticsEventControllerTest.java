package faang.school.analytics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = AnalyticsEventController.class)
public class AnalyticsEventControllerTest {

    private final Long receiverId = 1L;
    private final EventType firstEventType = EventType.POST_LIKE;
    private final LocalDateTime now = LocalDateTime.now();

    @MockBean
    private AnalyticsEventService analyticsEventService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testPositiveSaveAnalytics() throws Exception {
        AnalyticsEventDto analytic = createAnalyticsEventDto(receiverId, firstEventType);

        mockMvc.perform(post("/analytics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(analytic)))
                .andExpect(status().isOk());
    }

    @Test
    void testPositiveGetAnalytics() throws Exception {
        AnalyticsEventFilterDto filter = createAnalyticsFilter();
        List<AnalyticsEventDto> analyticsList = List.of(
                createAnalyticsEventDto(receiverId, firstEventType),
                createAnalyticsEventDto(receiverId, firstEventType)
        );

        when(analyticsEventService.getAnalytics(filter)).thenReturn(analyticsList);

        mockMvc.perform(post("/analytics/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(mapper.writeValueAsString(analyticsList)));
    }

    private AnalyticsEventFilterDto createAnalyticsFilter() {
        return AnalyticsEventFilterDto.builder()
                .receiverId(receiverId)
                .eventType(firstEventType)
                .interval(Interval.MONTH)
                .from(null)
                .to(null)
                .build();
    }

    private AnalyticsEventDto createAnalyticsEventDto(Long receiverId, EventType eventType) {
        return AnalyticsEventDto.builder()
                .receiverId(receiverId)
                .actorId(2L)
                .eventType(eventType)
                .receivedAt(now)
                .build();
    }
}
