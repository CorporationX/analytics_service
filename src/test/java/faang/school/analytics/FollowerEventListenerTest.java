package faang.school.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.listener.FollowerEventListener;
import faang.school.analytics.mapper.FollowerEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowerEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsService;

    @Mock
    private FollowerEventMapper followerEventMapper;

    @InjectMocks
    private FollowerEventListener listener;


    @Test
    void testPositiveSendAnalytics() throws Exception {
        FollowerEvent event = new FollowerEvent(1L, 2L, LocalDateTime.now());
        AnalyticsEventDto dto;
        dto = new AnalyticsEventDto(
                2L, 1L, EventType.FOLLOWER, event.subscriptionDate());

        ObjectMapper realObjectMapper = new ObjectMapper();
        realObjectMapper.registerModule(new JavaTimeModule());
        byte[] messageBody;
        messageBody = realObjectMapper.writeValueAsBytes(event);
        byte[] channel = "followerEventTopic".getBytes(StandardCharsets.UTF_8);

        Message redisMessage = new Message() {
            @Override
            public byte[] getBody() {
                return messageBody;
            }

            @Override
            public byte[] getChannel() {
                return channel;
            }
        };

        when(objectMapper.readValue(messageBody, FollowerEvent.class)).thenReturn(event);
        when(followerEventMapper.toAnalyticsDto(event)).thenReturn(dto);

        listener.onMessage(redisMessage, null);

        verify(analyticsService).saveEvent(dto);
    }

}
