package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.ProjectViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectViewEventListenerTest {
    @InjectMocks
    private ProjectViewEventListener projectViewEventListener;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Test
    public void testOnMessage() {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        ProjectViewEventDto eventDto = ProjectViewEventDto.builder()
                                        .projectId(1L)
                                        .build();
        Message message = mock(Message.class);

        when(message.getBody()).thenReturn("".getBytes());
        try {
            when(objectMapper.readValue(message.getBody(), ProjectViewEventDto.class)).thenReturn(eventDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        when(analyticsEventMapper.toProjectEntity(eventDto)).thenReturn(analyticsEvent);

        projectViewEventListener.onMessage(message, null);

        verify(analyticsEventMapper, times(1)).toProjectEntity(eventDto);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
    }
}