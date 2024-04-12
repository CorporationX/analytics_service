package faang.school.analytics.listener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;

@ExtendWith(MockitoExtension.class)
public class ProjectViewEventListenerTest {

    @Test
    public void testOnMessage() throws JsonProcessingException {
        Message mockMessage = Mockito.mock(Message.class);
        byte[] mockPattern = new byte[0];

        String userId = "1";
        String projectId = "2";
        String timestamp = "2024-04-12T00:00:00";
        String jsonMessage = "{\"userId\":\"" + userId + "\",\"projectId\":\"" + projectId + "\",\"timestamp\":\"" + timestamp + "\"}";
        ProjectViewEventListener mockListener = Mockito.mock(ProjectViewEventListener.class);

        mockListener.onMessage(mockMessage, mockPattern);

        Mockito.verify(mockListener, Mockito.times(1)).onMessage(mockMessage, mockPattern);
    }
}
