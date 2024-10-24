package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.ProjectViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectViewEventDtoListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Spy
    private AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);

    @Mock
    private AnalyticsEventServiceImpl analyticsEventService;

    @InjectMocks
    private ProjectViewEventListener projectViewEventListener;

    private Message message;
    private ProjectViewEventDto projectViewEventDto;

    @BeforeEach
    public void setUp() {
        String event = "{\"userId\":1,\"guestId\":2,\"viewDateTime\":\"2024-10-19T14:30:50\"}";
        message = new DefaultMessage("project_view_channel".getBytes(), event.getBytes());
        projectViewEventDto = new ProjectViewEventDto();
        projectViewEventDto.setReceiverId(1L);
        projectViewEventDto.setActorId(2L);
        projectViewEventDto.setTimestamp(LocalDateTime.of(2024, 10, 19, 14, 30, 50));
    }

    @Test
    public void testHandleEventFail() {
        message = new DefaultMessage("testChannel".getBytes(), "Test".getBytes());
        try {
            when(objectMapper.readValue(message.getBody(), ProjectViewEventDto.class))
                    .thenThrow(RuntimeException.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertThrows(RuntimeException.class, () -> projectViewEventListener.handleEvent(message, ProjectViewEventDto.class));
    }

    @Test
    public void testHandleEventSuccess() {
        try {
            when(objectMapper.readValue(message.getBody(), ProjectViewEventDto.class))
                    .thenReturn(projectViewEventDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ProjectViewEventDto testProjectViewEventDto = projectViewEventListener.handleEvent(message, ProjectViewEventDto.class);
        Assertions.assertNotNull(testProjectViewEventDto);
        Assertions.assertEquals(projectViewEventDto.getReceiverId(), testProjectViewEventDto.getReceiverId());
        Assertions.assertEquals(projectViewEventDto.getActorId(), testProjectViewEventDto.getActorId());
        Assertions.assertEquals(projectViewEventDto.getTimestamp(), testProjectViewEventDto.getTimestamp());
    }

    @Test
    public void testSendAnalyticsSuccess() {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(projectViewEventDto);
        analyticsEvent.setEventType(EventType.PROJECT_VIEW);
        projectViewEventListener.sendAnalytics(projectViewEventDto);

        verify(analyticsEventService).saveEvent(analyticsEvent);
    }
}
