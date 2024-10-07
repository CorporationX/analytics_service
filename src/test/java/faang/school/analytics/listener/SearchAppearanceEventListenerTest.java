package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.dto.SearchAppearanceEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchAppearanceEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private Message message;
    @InjectMocks
    private SearchAppearanceEventListener listener;

    @Test
    @DisplayName("Checking it is method called with the correct arguments and returns the expected object")
    public void testOnMessage_Success() throws IOException {
        byte[] pattern = new byte[]{};

        when(objectMapper.readValue(message.getBody(), SearchAppearanceEvent.class)).thenReturn(new SearchAppearanceEvent());
        when(analyticsEventMapper.toAnalyticsEvent(any(SearchAppearanceEvent.class))).thenReturn(new AnalyticsEvent());

        listener.onMessage(message, pattern);
        verify(analyticsEventService).saveAnalyticsEvent(Mockito.any(AnalyticsEvent.class));
    }

    @Test
    @DisplayName("Checking that the method throws an exception")
    public void testOnMessage_FailedSerialization() throws IOException {
        byte[] pattern = new byte[]{};

        when(objectMapper.readValue(message.getBody(), FollowerEvent.class)).thenThrow(IOException.class);
        Assert.assertThrows(RuntimeException.class, () -> listener.onMessage(message, pattern));

    }
}