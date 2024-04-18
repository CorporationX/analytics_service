package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.SearchAppearanceEvent;
import faang.school.analytics.listeners.SearchAppearanceEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchAppearanceEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private Message message;
    @InjectMocks
    private SearchAppearanceEventListener listener;

    private SearchAppearanceEvent searchAppearanceEvent;
    private AnalyticsEvent analyticsEvent;
    private byte[] json;

    @BeforeEach
    public void setUp() {
        json = new byte[]{};
        searchAppearanceEvent = SearchAppearanceEvent.builder()
                .viewedUserId(1L)
                .viewerUserId(2L)
                .viewingTime(LocalDateTime.now())
                .build();

        analyticsEvent = AnalyticsEvent.builder()
                .receiverId(1L)
                .actorId(2L)
                .receivedAt(LocalDateTime.now())
                .build();

    }

    @Test
    @DisplayName("Checking it is method called with the correct arguments and returns the expected object")
    public void testReadValue() throws IOException {
        when(objectMapper.readValue(json, SearchAppearanceEvent.class)).thenReturn(searchAppearanceEvent);

        listener.readValue(json, SearchAppearanceEvent.class);

        verify(objectMapper).readValue(json, searchAppearanceEvent.getClass());
    }

    @Test
    @DisplayName("Checking that the object is saved")
    public void testSave() {
        when(analyticsEventRepository.save(analyticsEvent)).thenReturn(analyticsEvent);

        listener.save(analyticsEvent);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    @DisplayName("Checking the correctness works of the method")
    public void testOnMessage() {
        when(listener.readValue(message.getBody(), SearchAppearanceEvent.class)).thenReturn(searchAppearanceEvent);
        when(analyticsEventMapper.entityToAnalyticsEvent(searchAppearanceEvent)).thenReturn(analyticsEvent);

        listener.onMessage(message, json);

        verify(analyticsEventMapper, times(1)).entityToAnalyticsEvent(searchAppearanceEvent);
    }

    @Test
    @DisplayName("Checking that the method throws an exception")
    public void testListener_FailedSerialization() throws IOException {
        when(objectMapper.readValue(json, SearchAppearanceEvent.class)).thenThrow(new IOException());
        assertThrows(RuntimeException.class, () -> listener.readValue(json, SearchAppearanceEvent.class));
    }
}
