package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.filter.AnalyticsEventIntervalFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.List;
import java.util.stream.Stream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {
    @InjectMocks
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private AnalyticsEventIntervalFilter analyticsEventIntervalFilter;
    @Mock
    private ObjectMapper objectMapper;


    private AnalyticsEvent analyticsEvent;
    private AnalyticsEventDto analyticsEventDto;
    private Stream<AnalyticsEvent> analyticsEventStream;
    private long receiverId;
    private EventType eventType;
    private AnalyticsEventFilterDto analyticsEventFilterDto;

    @BeforeEach
    public void setUp() {
        receiverId = 1L;
        eventType = EventType.FOLLOWER;
        analyticsEvent = AnalyticsEvent.builder().build();
        analyticsEventStream = Stream.of(analyticsEvent);
        analyticsEventDto = AnalyticsEventDto.builder().build();
        analyticsEventFilterDto = AnalyticsEventFilterDto
                .builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .build();

        analyticsEventService = AnalyticsEventService.builder()
                .analyticsEventMapper(analyticsEventMapper)
                .analyticsEventRepository(analyticsEventRepository)
                .analyticsEventFilters(List.of(analyticsEventIntervalFilter))
                .objectMapper(objectMapper)
                .build();
    }

    @Test
    @DisplayName("testing saveEvent method execution")
    void testSaveEvent() {
        analyticsEventService.saveEvent(analyticsEvent);
        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    @DisplayName("testing getAnalytics method with interval filter")
    void testGetAnalyticsWithIntervalFilter() {
        when(analyticsEventRepository.findByReceiverIdAndEventTypeOrderByReceiverIdDesc(receiverId, eventType))
                .thenReturn(analyticsEventStream);
        when(analyticsEventIntervalFilter.filter(analyticsEventStream, analyticsEventFilterDto))
                .thenReturn(analyticsEventStream);
        when(analyticsEventMapper.toDto(analyticsEvent)).thenReturn(analyticsEventDto);

        List<AnalyticsEventDto> analyticsEvents =
                analyticsEventService.getAnalytics(analyticsEventFilterDto);

        verify(analyticsEventRepository, times(1))
                .findByReceiverIdAndEventTypeOrderByReceiverIdDesc(receiverId, eventType);
        verify(analyticsEventIntervalFilter, times(1))
                .filter(analyticsEventStream, analyticsEventFilterDto);
        verify(analyticsEventMapper, times(1)).toDto(analyticsEvent);
        assertNotNull(analyticsEvents);
        assertIterableEquals(List.of(analyticsEventDto), analyticsEvents);
    }

    @DisplayName("testing saveLikeAnalytics method")
    @Test
    public void testSaveLikeAnalytics() throws IOException {
        Message message = mock(Message.class);
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        LikeEvent likeEvent = new LikeEvent();

        when(objectMapper.readValue(message.getBody(), LikeEvent.class)).thenReturn(likeEvent);
        when(analyticsEventMapper.toAnalyticsEventFromLikeEvent(likeEvent)).thenReturn(analyticsEvent);

        analyticsEventService.saveLikeAnalytics(message);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }
}