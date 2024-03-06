package faang.school.analytics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.SearchAppearanceEvent;
import faang.school.analytics.listener.SearchAppearanceEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchAppearanceEventListenerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private SearchAppearanceEventListener searchAppearanceEventListener;

    @Test
    void testOnMessageWhenValidMessageThenSaveAnalyticsEvent() throws IOException {
        SearchAppearanceEvent searchAppearanceEvent = new SearchAppearanceEvent(
                1L,
                2L,
                LocalDateTime.now());

        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        analyticsEvent.setReceiverId(1L);
        analyticsEvent.setActorId(2L);
        analyticsEvent.setReceivedAt(LocalDateTime.now());

        Message message = mock(Message.class);
        when(message.getBody()).thenReturn("valid message".getBytes());
        when(objectMapper.readValue(any(String.class), eq(SearchAppearanceEvent.class)))
                .thenReturn(searchAppearanceEvent);
        when(analyticsEventMapper.toAnalyticsEvent(searchAppearanceEvent)).thenReturn(analyticsEvent);

        searchAppearanceEventListener.onMessage(message, new byte[0]);

        verify(analyticsEventService).save(analyticsEvent);
    }
}