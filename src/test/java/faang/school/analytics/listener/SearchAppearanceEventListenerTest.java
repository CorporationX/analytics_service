package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.SearchAppearanceEventDto;
import faang.school.analytics.mapper.search_appearance_event.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.search_appearance_event.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchAppearanceEventListenerTest {
    @InjectMocks
    private SearchAppearanceEventListener searchAppearanceEventListener;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private Message message;
    private SearchAppearanceEventDto searchAppearanceEventDto;
    private AnalyticsEvent analyticsEvent;
    private byte [] jsonPattern;


    @BeforeEach
    void setUp() {
        jsonPattern = new byte[]{};
        searchAppearanceEventDto = SearchAppearanceEventDto.builder()
                .receiverId(1L)
                .actorId(2L)
                .receivedAt(LocalDateTime.now())
                .build();
        analyticsEvent = AnalyticsEvent.builder()
                .receiverId(1L)
                .actorId(2L)
                .receivedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testOnMessage() {
        when(searchAppearanceEventListener.readValue(message.getBody(), SearchAppearanceEventDto.class)).thenReturn(searchAppearanceEventDto);
        when(analyticsEventMapper.toEntity(searchAppearanceEventDto)).thenReturn(analyticsEvent);

        searchAppearanceEventListener.onMessage(message, jsonPattern);

        verify(analyticsEventMapper, times(1)).toEntity(searchAppearanceEventDto);
    }

    @Test
    void testReadValue() throws IOException {
        when(objectMapper.readValue(jsonPattern, SearchAppearanceEventDto.class)).thenReturn(searchAppearanceEventDto);

        searchAppearanceEventListener.readValue(jsonPattern, SearchAppearanceEventDto.class);

        verify(objectMapper).readValue(jsonPattern, searchAppearanceEventDto.getClass());
    }

    @Test
    void testSave() {
        when(analyticsEventRepository.save(analyticsEvent)).thenReturn(analyticsEvent);

        searchAppearanceEventListener.save(analyticsEvent);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    void testToObject_FailedSerialization() throws IOException {
        when(objectMapper.readValue(jsonPattern, SearchAppearanceEventDto.class)).thenThrow(new IOException());
        assertThrows(RuntimeException.class, () -> searchAppearanceEventListener.readValue(jsonPattern, SearchAppearanceEventDto.class));
    }
}