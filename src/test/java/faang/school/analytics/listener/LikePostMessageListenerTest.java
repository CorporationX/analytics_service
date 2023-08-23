package faang.school.analytics.listener;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.JsonObjectMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class LikePostMessageListenerTest {

    @InjectMocks
    LikePostMessageListener likePostMessageListener;
    @Mock
    JsonObjectMapper jsonObjectMapper;
    @Mock
    AnalyticsEventService analyticsEventService;

    @Test
    void onMessage() {
        AnalyticsEventDto eventDto = AnalyticsEventDto.builder().build();
        String a = "message";
        byte[] message = a.getBytes();
        Mockito.when(jsonObjectMapper.readValue(message, AnalyticsEventDto.class)).thenReturn(eventDto);

        likePostMessageListener.onMessage(new Message() {
            @Override
            public byte[] getBody() {
                return message;
            }

            @Override
            public byte[] getChannel() {
                return new byte[0];
            }
        }, message);
        Mockito.verify(analyticsEventService).saveEvent(eventDto);
        AnalyticsEventDto eventDtoExpected = AnalyticsEventDto.builder().eventType(EventType.POST_LIKE).build();
        assertEquals(eventDtoExpected,eventDto);
    }
}