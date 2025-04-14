package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listener.data.TestEventListener;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbstractEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private Message message;

    @InjectMocks
    private TestEventListener eventListener;

    @BeforeEach
    public void setUp() {
        eventListener = new TestEventListener(objectMapper, analyticsEventService);
    }

    @Test
    @DisplayName("Проверка метода onMessage на успешное выполенение")
    public void givenValidData_whenOnMessage_thenSuccess() throws Exception {
        String testMessage = "testMessage";
        byte[] messageBytes = testMessage.getBytes();

        when(message.getBody()).thenReturn(messageBytes);
        when(objectMapper.readValue(messageBytes, String.class)).thenReturn(testMessage);

        assertDoesNotThrow(() -> eventListener.onMessage(message, null));

        verify(objectMapper).readValue(messageBytes, String.class);
    }

    @Test
    @DisplayName("Проверка метода onMessage на ошибку IOException")
    public void givenValidData_whenOnMessage_thenThrowException() throws Exception {
        String testMessage = "testMessage";
        byte[] messageBytes = testMessage.getBytes();

        when(message.getBody()).thenReturn(messageBytes);
        when(objectMapper.readValue(messageBytes, String.class)).thenThrow(new IOException());

        assertThrows(RuntimeException.class, () -> eventListener.onMessage(message, null));
    }
}