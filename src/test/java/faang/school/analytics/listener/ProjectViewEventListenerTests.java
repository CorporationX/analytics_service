package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.ProjectViewEvent;
import faang.school.analytics.mapper.project_view.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.event.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectViewEventListenerTests {
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProjectViewEventListener listener;

    private ProjectViewEvent projectViewEvent;
    private String messageString;
    @Mock
    private Message message;
    String jsonRepresentation = "{"
            + "\"userId\": 1,"
            + "\"projectId\": 123,"
            + "\"timestamp\": \"2023-10-10T15:30:00\""
            + "}";

    @BeforeEach
    void setUp() {
        projectViewEvent = new ProjectViewEvent(1L, 2L, LocalDateTime.now());
    }

    @Test
    void testHandleEventShouldMapAndAddAnalyticsEvent() {
        when(analyticsEventMapper.toEntity(any(ProjectViewEvent.class))).thenReturn(new AnalyticsEvent());
        listener.handleEvent(projectViewEvent);

        verify(analyticsEventService).addNewEvent(any(AnalyticsEvent.class));
    }

    @Test
    void testOnMessage_ShouldProcessMessage() throws com.fasterxml.jackson.core.JsonProcessingException {
        when(message.getBody()).thenReturn(jsonRepresentation.getBytes());
        when(objectMapper.readValue(jsonRepresentation, ProjectViewEvent.class)).thenReturn(projectViewEvent);
        when(analyticsEventMapper.toEntity(any(ProjectViewEvent.class))).thenReturn(new AnalyticsEvent());

        listener.onMessage(message, null);

        verify(analyticsEventMapper).toEntity(any(ProjectViewEvent.class));
        verify(analyticsEventService).addNewEvent(any(AnalyticsEvent.class));
    }

}
