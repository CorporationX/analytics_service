package faang.school.analytics.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.kafka.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowerConsumerTest {
    @InjectMocks
    private FollowerConsumer followerConsumer;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Test
    public void consumeEventSuccessfullySavesFollowerUser() {
        long anyLong = 1L;
        int anyInt = 1;
        String anyString = "anyString";
        FollowerEvent anyFollowerEvent = new FollowerEvent(anyLong, anyLong, null);
        ConsumerRecord<String, Object> anyConsumerRecord = new ConsumerRecord<>(
                anyString, anyInt, anyLong, anyString, anyFollowerEvent
        );
        when(objectMapper.convertValue(anyConsumerRecord.value(), FollowerEvent.class)).thenReturn(anyFollowerEvent);

        followerConsumer.consumeEvent(anyConsumerRecord);
        verify(objectMapper, times(1))
                .convertValue(anyConsumerRecord.value(), FollowerEvent.class);
        verify(analyticsEventService, times(1)).saveEvent(any(AnalyticsEvent.class));
    }

    @Test
    public void consumeEventSuccessfullySavesFollowerProject() {
        long anyLong = 1L;
        int anyInt = 1;
        String anyString = "anyString";
        FollowerEvent anyFollowerEvent = new FollowerEvent(anyLong, null, anyLong);
        ConsumerRecord<String, Object> anyConsumerRecord = new ConsumerRecord<>(
                anyString, anyInt, anyLong, anyString, anyFollowerEvent
        );

        when(objectMapper.convertValue(anyConsumerRecord.value(), FollowerEvent.class)).thenReturn(anyFollowerEvent);
        followerConsumer.consumeEvent(anyConsumerRecord);
        verify(objectMapper, times(1))
                .convertValue(anyConsumerRecord.value(), FollowerEvent.class);
        verify(analyticsEventService, times(1)).saveEvent(any(AnalyticsEvent.class));
    }
}
