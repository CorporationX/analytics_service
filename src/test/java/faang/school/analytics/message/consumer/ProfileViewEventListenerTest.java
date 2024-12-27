package faang.school.analytics.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.exception.EventProcessingException;
import faang.school.analytics.exception.MessageMappingException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.message.event.ProfileViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProfileViewEventListenerTest {
    private ProfileViewEventListener profileViewEventListener;
    private ObjectMapper objectMapper;
    private AnalyticsEventService analyticsEventService;
    private AnalyticsEventMapper analyticsEventMapper;

    private Message message;
    private byte[] messageBody;

    @BeforeEach
    public void setUp() {
        objectMapper = Mockito.mock(ObjectMapper.class);
        analyticsEventService = Mockito.mock(AnalyticsEventService.class);
        analyticsEventMapper = new AnalyticsEventMapperImpl();
        profileViewEventListener =
                new ProfileViewEventListener(objectMapper, analyticsEventService, analyticsEventMapper);
        message = Mockito.mock(Message.class);
        messageBody = new byte[]{};
    }

    @Test
    public void testOnMessage() throws IOException {
        // arrange
        long actorId = 5L;
        long receiverId = 2L;
        LocalDateTime receivedAt = LocalDateTime.now();
        ProfileViewEvent profileViewEvent = ProfileViewEvent.builder()
                .actorId(actorId)
                .receiverId(receiverId)
                .receivedAt(receivedAt)
                .build();
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .receiverId(receiverId)
                .actorId(actorId)
                .eventType(EventType.PROFILE_VIEW)
                .receivedAt(receivedAt)
                .build();

        when(message.getBody()).thenReturn(messageBody);
        doReturn(profileViewEvent).when(objectMapper).readValue(messageBody, ProfileViewEvent.class);

        // act
        profileViewEventListener.onMessage(message, new byte[]{});

        // assert
        verify(analyticsEventService).saveEvent(analyticsEvent);
    }

    @Test
    public void testOnMessageThrowsMessageMappingException() throws IOException {
        // arrange
        when(message.getBody()).thenReturn(messageBody);
        doThrow(IOException.class)
                .when(objectMapper)
                .readValue(messageBody, ProfileViewEvent.class);

        // act and assert
        assertThrows(EventProcessingException.class,
                () -> profileViewEventListener.onMessage(message, new byte[]{}));
    }
}
