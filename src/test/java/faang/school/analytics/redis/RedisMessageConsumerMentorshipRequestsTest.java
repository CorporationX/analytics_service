package faang.school.analytics.redis;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RedisMessageConsumerMentorshipRequestsTest {

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private Message message;

    @InjectMocks
    private RedisMessageConsumerMentorshipRequests redisMessageConsumerMentorshipRequests;


    @BeforeEach
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.findAndRegisterModules();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testOnMessage() throws IOException {
        String json = "{\"requesterId\": 12345, \"receiverId\": 67890, \"requestTime\": \"2024-10-12T10:00:00\"}";

        // Создаем объект MentorshipRequestEvent, который мы ожидаем получить после десериализации
        MentorshipRequestEvent expectedEvent = new MentorshipRequestEvent();
        expectedEvent.setRequesterId(12345L);
        expectedEvent.setReceiverId(67890L);
        expectedEvent.setRequestTime(LocalDateTime.parse("2024-10-12T10:00:00"));

        // Мокаем поведение message
        byte[] nameChannel = "nameChannel".getBytes();
        byte[] messageBytes = json.getBytes();

        when(message.getBody()).thenReturn(messageBytes); // Мокируем тело сообщения
        when(message.getChannel()).thenReturn(nameChannel);
        // Вызов метода onMessage, который должен десериализовать JSON и сохранить событие
        redisMessageConsumerMentorshipRequests.onMessage(message, null);

        // Каптурим переданное событие
        ArgumentCaptor<MentorshipRequestEvent> eventCaptor = ArgumentCaptor.forClass(MentorshipRequestEvent.class);
        verify(analyticsEventService).saveAnalyticEvent(eventCaptor.capture());

        // Проверяем, что сохраненный объект совпадает с ожидаемым
        assertEquals(expectedEvent.getRequesterId(), eventCaptor.getValue().getRequesterId());
        assertEquals(expectedEvent.getReceiverId(), eventCaptor.getValue().getReceiverId());
        assertEquals(expectedEvent.getRequestTime(), eventCaptor.getValue().getRequestTime());
    }
}

