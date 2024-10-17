package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.ProjectViewEvent;
import faang.school.analytics.mapper.analytics.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.analytics_event.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectViewEventListenerTest {

    private static final long ACTOR_ID = 1L;
    private static final long RECEIVER_ID = 1L;

    @InjectMocks
    private ProjectViewEventListener projectViewEventListener;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private Message message;

    private ProjectViewEvent projectViewEvent;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void init() {
        projectViewEvent = new ProjectViewEvent();
        projectViewEvent.setActorId(ACTOR_ID);
        projectViewEvent.setReceiverId(RECEIVER_ID);
        projectViewEvent.setReceivedAt(LocalDateTime.now());

        analyticsEvent = AnalyticsEvent.builder()
                .receiverId(RECEIVER_ID)
                .actorId(ACTOR_ID)
                .receivedAt(projectViewEvent.getReceivedAt())
                .build();
    }

    @Test
    @DisplayName("Should save event in database")
    void whenGotMessageThenSaveItInDatabase() throws IOException {
        when(objectMapper.readValue(message.getBody(), ProjectViewEvent.class))
                .thenReturn(projectViewEvent);
        when(analyticsEventMapper.toAnalyticsEntity(projectViewEvent))
                .thenReturn(analyticsEvent);

        projectViewEventListener.onMessage(message, new byte[0]);

        verify(objectMapper)
                .readValue(message.getBody(), ProjectViewEvent.class);
        verify(analyticsEventMapper)
                .toAnalyticsEntity(projectViewEvent);
        verify(analyticsEventService)
                .saveEvent(analyticsEvent);
    }

    @Test
    @DisplayName("When trying handle incoming message and user object mapper then throw exception")
    void whenIncomingMessageInObjectMapperErrorThenThrowException() throws IOException {
        when(objectMapper.readValue(message.getBody(), ProjectViewEvent.class))
                .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class,
                () -> projectViewEventListener.onMessage(message, new byte[0]));
    }

    @Test
    @DisplayName("When trying map abstract event to analytics event then throw exception")
    void whenEventMapToEventMappingErrorThenThrowException() throws IOException {
        when(objectMapper.readValue(message.getBody(), ProjectViewEvent.class))
                .thenReturn(projectViewEvent);
        when(analyticsEventMapper.toAnalyticsEntity(projectViewEvent))
                .thenThrow(MappingException.class);

        assertThrows(MappingException.class,
                () -> projectViewEventListener.onMessage(message, new byte[0]));
    }
}