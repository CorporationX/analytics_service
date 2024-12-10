package faang.school.analytics.listenerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.SearchAppearanceEvent;
import faang.school.analytics.listener.SearchAppearanceEventListener;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SearchAppearanceEventListenerTest {

    @Mock
    private AnalyticsEventService service;

    @Mock
    private ObjectMapper objectMapper;

    private SearchAppearanceEventListener listener;

    @Test
    void onMessage() throws Exception {
        MockitoAnnotations.openMocks(this);
        listener = new SearchAppearanceEventListener(service, objectMapper);

        Message mockMessage = mock(Message.class);
        byte[] mockBody = "{ \"userId\": 1, \"searchingUserId\": 2, \"viewedAt\": \"2024-12-04T10:30\" }".getBytes();
        when(mockMessage.getBody()).thenReturn(mockBody);

        SearchAppearanceEvent mockEvent = new SearchAppearanceEvent();
        mockEvent.setUserId(1L);
        mockEvent.setSearchingUserId(2L);
        mockEvent.setViewedAt(LocalDateTime.of(2024, 12, 4, 10, 30));
        when(objectMapper.readValue(mockBody, SearchAppearanceEvent.class)).thenReturn(mockEvent);

        listener.onMessage(mockMessage, null);

        verify(service, times(1)).processEvent(mockEvent);
    }

    @Test
    void onMessage_withException() throws Exception {
        MockitoAnnotations.openMocks(this);
        listener = new SearchAppearanceEventListener(service, objectMapper);

        Message mockMessage = mock(Message.class);
        byte[] mockBody = "invalid json".getBytes();
        when(mockMessage.getBody()).thenReturn(mockBody);

        when(objectMapper.readValue(mockBody, SearchAppearanceEvent.class)).thenThrow(new RuntimeException("JSON parsing error"));

        listener.onMessage(mockMessage, null);

        verify(service, never()).processEvent(any());
    }
}