package faang.school.analytics.controller.analyticsevent;

import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.enums.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventControllerTest {
    @InjectMocks
    private AnalyticsEventController analyticsEventController;

    @Mock
    private AnalyticsEventService analyticsEventService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(analyticsEventController).build();
    }

    @Test
    @DisplayName("Get Analytics Events Test")
    void testGetAnalyticsEvents() throws Exception {
        var analyticsEventDto = AnalyticsEventDto.builder()
                .id(1L)
                .receiverId(2L)
                .actorId(3L)
                .eventType(EventType.PROFILE_VIEW)
                .receivedAt(LocalDateTime.parse("2024-06-01T15:30:00"))
                .build();

    doReturn(List.of(analyticsEventDto, analyticsEventDto)).when(analyticsEventService)
            .getAnalytics(anyLong(), any(EventType.class), any(Interval.class),
                    any(LocalDateTime.class), any(LocalDateTime.class));

        mockMvc.perform(get("/api/v1/analytics-events")
                .param("receiver-id", "1")
                .param("event-type", "PROFILE_VIEW")
                .param("interval", "YEAR")
                .param("from", "2023-12-01T00:00:00.000")
                .param("to", "2024-12-01T00:00:00.000"))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$[0].id").value(1),
                        jsonPath("$[0].receiverId").value(2),
                        jsonPath("$[0].actorId").value(3),
                        jsonPath("$[0].eventType").value("PROFILE_VIEW"),
                        jsonPath("$[0].receivedAt").value("2024-06-01T15:30:00")
                );

    }
}