package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.analyticsevent.AnalyticsEventMapperImpl;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.event.ProjectViewEvent;
import faang.school.analytics.service.impl.analyticsevent.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectViewEventListenerTest {

    @Mock
    private AnalyticsEventServiceImpl analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;

    @InjectMocks
    private ProjectViewEventListener projectViewEventListener;

    private Message message;
    private ProjectViewEvent projectViewEvent;
    String json;

    @BeforeEach
    void setUp() {
        projectViewEvent = ProjectViewEvent.builder()
                .projectId(1L)
                .userId(2L)
                .visitTime(LocalDateTime.of(2024, 10, 14, 13, 56, 39, 0))
                .build();
        json = "{\"projectId\":1, \"userId\":2, \"visitTime\":2016-03-16T13:56:39.492}";
        message = mock(Message.class);
        when(message.getBody()).thenReturn(json.getBytes());
    }

    @Test
    void onMessage_shouldHandleEventSuccessfully() throws IOException {
        when(objectMapper.readValue(json.getBytes(), ProjectViewEvent.class)).thenReturn(projectViewEvent);

        projectViewEventListener.onMessage(message, null);
        // then
        verify(analyticsEventMapper).toEntity(projectViewEvent);
        verify(analyticsEventService).saveEvent(any(AnalyticsEvent.class));
        verify(analyticsEventService).saveEvent(argThat(event -> event.getEventType() == EventType.PROJECT_VIEW));
        verify(analyticsEventService).saveEvent(argThat(event -> event.getReceivedAt().equals(projectViewEvent.visitTime())));
    }
}
