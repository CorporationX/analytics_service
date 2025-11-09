package faang.school.analytics.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.kafka.consumer.GoalCompletedEventConsumer;
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
public class GoalCompletedEventConsumerTest {
    @InjectMocks
    private GoalCompletedEventConsumer goalCompletedEventConsumer;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Test
    public void consumeEventSuccessfullySavesEvent() {
        long anyLong = 1L;
        int anyInt = 1;
        String anyString = "anyString";
        GoalCompletedEvent anyGoalCompletedEvent = new GoalCompletedEvent(anyLong, anyLong);
        ConsumerRecord<String, Object> anyConsumerRecord = new ConsumerRecord<>(
                anyString, anyInt, anyLong, anyString, anyGoalCompletedEvent
        );
        when(objectMapper.convertValue(anyConsumerRecord.value(), GoalCompletedEvent.class))
                .thenReturn(anyGoalCompletedEvent);
        goalCompletedEventConsumer.consumeEvent(anyConsumerRecord);
        verify(objectMapper, times(1))
                .convertValue(anyConsumerRecord.value(), GoalCompletedEvent.class);
        verify(analyticsEventService, times(1)).saveEvent(any(AnalyticsEvent.class));
    }
}
