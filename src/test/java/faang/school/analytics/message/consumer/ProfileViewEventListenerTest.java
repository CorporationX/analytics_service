package faang.school.analytics.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.message.event.event.ProfileViewEvent;
import faang.school.analytics.exception.MessageMappingException;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProfileViewEventListenerTest {
    private ProfileViewEventListener profileViewEventListener;
    private ObjectMapper objectMapper;
    private AnalyticsEventService analyticsEventService;

    private Message message;
    private byte[] messageBody;

    @BeforeEach
    public void setUp() {
        objectMapper = Mockito.mock(ObjectMapper.class);
        analyticsEventService = Mockito.mock(AnalyticsEventService.class);
        profileViewEventListener = new ProfileViewEventListener(objectMapper, analyticsEventService);

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

        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, ProfileViewEvent.class))
                .thenReturn(profileViewEvent);

        // act
        profileViewEventListener.onMessage(message, new byte[]{});

        // assert
        verify(analyticsEventService).saveProfileView(profileViewEvent);
    }

    @Test
    public void testOnMessageThrowsMessageMappingException() throws IOException {
        // arrange
        when(message.getBody()).thenReturn(messageBody);
        doThrow(IOException.class)
                .when(objectMapper)
                .readValue(messageBody, ProfileViewEvent.class);

        // act and assert
        assertThrows(MessageMappingException.class,
                () -> profileViewEventListener.onMessage(message, new byte[]{}));
    }
}
