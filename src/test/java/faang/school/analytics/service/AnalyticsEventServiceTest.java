package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;

    private long receiverId;
    private EventType eventType;
    private AnalyticsEvent analyticsEvent;
    private AnalyticsEventDto analyticsEventDto;
    private AnalyticsEventDto firstAnalyticsEventDto;
    private AnalyticsEventDto secondAnalyticsEventDto;
    private AnalyticsEventDto thirdAnalyticsEventDto;
    private Interval interval;
    private LocalDateTime from;
    private LocalDateTime to;
    private Stream<AnalyticsEvent> analyticsEvents;

    @BeforeEach
    public void setup() {
        receiverId = 0;
        eventType = EventType.POST_LIKE;

        analyticsEvent = new AnalyticsEvent();
        analyticsEventDto = AnalyticsEventDto.builder().build();

        LocalDateTime currentDateTime = LocalDateTime.now();

        AnalyticsEvent firstAnalyticsEvent = AnalyticsEvent.builder()
            .receivedAt(currentDateTime.plusDays(1))
            .build();
        AnalyticsEvent secondAnalyticsEvent = AnalyticsEvent.builder()
            .receivedAt(currentDateTime.plusDays(30))
            .build();
        AnalyticsEvent thirdAnalyticsEvent = AnalyticsEvent.builder()
            .receivedAt(currentDateTime.plusDays(6))
            .build();

        firstAnalyticsEventDto = AnalyticsEventDto.builder()
            .id(0L)
            .receivedAt(currentDateTime.plusDays(1))
            .build();
        secondAnalyticsEventDto = AnalyticsEventDto.builder()
            .id(0L)
            .receivedAt(currentDateTime.plusDays(30))
            .build();
        thirdAnalyticsEventDto = AnalyticsEventDto.builder()
            .id(0L)
            .receivedAt(currentDateTime.plusDays(6))
            .build();

        interval = Interval.WEEK;
        from = currentDateTime;
        to = currentDateTime.plusDays(31);

        analyticsEvents = Stream.of(firstAnalyticsEvent, secondAnalyticsEvent, thirdAnalyticsEvent);
    }

    @Test
    public void testSaveEvent() {
        when(analyticsEventMapper.toEntity(analyticsEventDto)).thenReturn(analyticsEvent);
        when(analyticsEventRepository.save(analyticsEvent)).thenReturn(analyticsEvent);
        when(analyticsEventMapper.toDto(analyticsEvent)).thenReturn(analyticsEventDto);

        AnalyticsEventDto result = analyticsEventService.saveEvent(analyticsEventDto);

        verify(analyticsEventMapper).toEntity(analyticsEventDto);
        verify(analyticsEventRepository).save(analyticsEvent);
        verify(analyticsEventMapper).toDto(analyticsEvent);

        assertNotNull(result);
        assertEquals(analyticsEventDto, result);
    }

    @Test
    public void testGetAnalytics_withInterval() {
        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(analyticsEvents);

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(receiverId, eventType, interval, null, null);

        assertNotNull(result);
        assertThat(result).containsExactly(firstAnalyticsEventDto, thirdAnalyticsEventDto);
    }

    @Test
    public void testGetAnalytics_withDateRange() {
        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(analyticsEvents);

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(receiverId, eventType, null, from, to);

        assertNotNull(result);
        assertThat(result).containsExactly(firstAnalyticsEventDto, thirdAnalyticsEventDto, secondAnalyticsEventDto);
    }
}
