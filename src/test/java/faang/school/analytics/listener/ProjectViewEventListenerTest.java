package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.ProjectViewEvent;
import faang.school.analytics.service.AnalyticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectViewEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticService analyticService;

    @InjectMocks
    private ProjectViewEventListener projectViewEventListener;

    private final String jsonEvent = "{\"projectId\":2,\"userId\":1,\"viewTime\":\"2024-10-07T13:32:29\"}";
    private ProjectViewEvent expectedEvent;

    @BeforeEach
    public void setUp() {
        expectedEvent = new ProjectViewEvent(2L, 1L, LocalDateTime.parse("2024-10-07T13:32:29"));
    }

    @Test
    public void handleMessage_shouldProcessEventSuccessfully() throws JsonProcessingException {
        when(objectMapper.readValue(jsonEvent, ProjectViewEvent.class)).thenReturn(expectedEvent);

        projectViewEventListener.handleMessage(jsonEvent);

        verify(analyticService).saveAnalyticEvent(expectedEvent);
    }

    @Test
    public void handleMessage_shouldLogErrorWhenJsonProcessingExceptionOccurs() throws JsonProcessingException {
        when(objectMapper.readValue(jsonEvent, ProjectViewEvent.class)).thenThrow(new JsonProcessingException("Error") {
        });

        assertThrows(RuntimeException.class, () -> projectViewEventListener.handleMessage(jsonEvent));

        verify(analyticService, never()).saveAnalyticEvent(any());
    }
}

