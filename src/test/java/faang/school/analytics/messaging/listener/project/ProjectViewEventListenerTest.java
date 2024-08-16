package faang.school.analytics.messaging.listener.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.project.ProjectViewEvent;
import faang.school.analytics.exception.event.DataTransformationException;
import faang.school.analytics.mapper.project.ProjectViewEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.analytics.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectViewEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private Message redisMessage;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private ProjectViewEventMapper projectViewEventMapper;
    @InjectMocks
    private ProjectViewEventListener sut;

    @Test
    void onMessage_shouldHandleEventAndSaveToDatabase() throws IOException {
        UUID eventId = UUID.randomUUID();
        var projectViewEvent = ProjectViewEvent.builder()
                .eventId(eventId)
                .projectId(1L)
                .viewerId(2L)
                .timestamp(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();
        var analyticsEvent = AnalyticsEvent.builder()
                .eventId(eventId)
                .receiverId(1L)
                .actorId(2L)
                .receivedAt(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();
        String jsonMessage = String.format(
                "{\"eventId\":\"%s\",\"projectId\":1,\"viewerId\":2,\"timestamp\":\"2021-01-01T00:00:00\"}",
                eventId
        );

        when(redisMessage.getBody()).thenReturn(jsonMessage.getBytes());
        when(objectMapper.readValue(jsonMessage.getBytes(), ProjectViewEvent.class)).thenReturn(projectViewEvent);
        when(projectViewEventMapper.toAnalyticsEntity(projectViewEvent)).thenReturn(analyticsEvent);

        sut.onMessage(redisMessage, new byte[0]);

        verify(objectMapper).readValue(jsonMessage.getBytes(), ProjectViewEvent.class);
        verify(analyticsEventService).saveEvent(analyticsEvent);

        assertEquals(EventType.PROJECT_VIEW, analyticsEvent.getEventType());
    }

    @Test
    void onMessage_shouldHandleException() throws Exception {
        String invalidJsonMessage = "invalid json";
        when(redisMessage.getBody()).thenReturn(invalidJsonMessage.getBytes());
        when(objectMapper.readValue(invalidJsonMessage.getBytes(), ProjectViewEvent.class))
                .thenThrow(new IOException("Invalid JSON"));

        assertThrows(DataTransformationException.class, () -> sut.onMessage(redisMessage, new byte[0]));
        verifyNoInteractions(analyticsEventService, projectViewEventMapper);
    }
}