package faang.school.analytics.kafka.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.kafka.ProjectViewEvent;
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
public class ProjectViewConsumerTest {
    @InjectMocks
    private ProjectViewConsumer projectViewConsumer;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Test
    public void consumeEventSuccessfullyConsumes() {
        long anyLong = 1L;
        int anyInt = 1;
        String anyString = "anyString";
        ProjectViewEvent anyProjectViewEvent = new ProjectViewEvent(anyLong, anyLong);
        ConsumerRecord<String, Object> anyConsumerRecord = new ConsumerRecord<>(
                anyString, anyInt, anyLong, anyString, anyProjectViewEvent
        );
        when(objectMapper.convertValue(anyConsumerRecord.value(), ProjectViewEvent.class))
                .thenReturn(anyProjectViewEvent);

        projectViewConsumer.consumeEvent(anyConsumerRecord);
        verify(objectMapper, times(1))
                .convertValue(anyConsumerRecord.value(), ProjectViewEvent.class);
        verify(analyticsEventService, times(1)).saveEvent(any(AnalyticsEvent.class));
    }
}
