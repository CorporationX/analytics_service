package faang.school.analytics.listener;

import faang.school.analytics.dto.EventDto;
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
public class MentorshipListenerTest {

    @InjectMocks
    MentorshipMessageListener mentorshipMessageListener;
    @Mock
    JsonObjectMapper jsonObjectMapper;
    @Mock
    AnalyticsEventService analyticsEventService;

    @Test
    void onMessage() {
        EventDto eventDto = EventDto.builder().build();
        String a = "message";
        byte[] message = a.getBytes();
        Mockito.when(jsonObjectMapper.readValue(message, EventDto.class)).thenReturn(eventDto);

        mentorshipMessageListener.onMessage(new Message() {
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
        EventDto eventDtoExpected = EventDto.builder().eventType(EventType.PROJECT_INVITE).build();
        assertEquals(eventDtoExpected,eventDto);
    }
}
