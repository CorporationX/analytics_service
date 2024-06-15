package faang.school.analytics.service;

import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.interval.Interval;
import faang.school.analytics.model.interval.TypeOfInterval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validator.AnalyticsEventValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceImplTest {
    private static final long FIRST_ANALYTICS_EVENT_ID = 1L;
    private static final long SECOND_ANALYTICS_EVENT_ID = 2L;
    private static final long RECEIVER_ID = 1L;
    private static final EventType EVENT_TYPE = EventType.PROJECT_INVITE;
    private static final LocalDateTime FROM = LocalDateTime.now().minusMonths(1);
    private static final LocalDateTime TO = LocalDateTime.now();

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private AnalyticsEventValidator analyticsEventValidator;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    private AnalyticsEvent firstAnalyticsEvent;
    private AnalyticsEvent secondAnalyticsEvent;
    private AnalyticsEventDto firstAnalyticsEventDto;
    private AnalyticsEventDto secondAnalyticsEventDto;


    @BeforeEach
    void setUp() {
        firstAnalyticsEvent = initAnalyticsEvent(FIRST_ANALYTICS_EVENT_ID, RECEIVER_ID, EVENT_TYPE)
                .receivedAt(LocalDateTime.now().minusMonths(1))
                .build();
        firstAnalyticsEventDto = initAnalyticsEventDto(FIRST_ANALYTICS_EVENT_ID, RECEIVER_ID, EVENT_TYPE)
                .receivedAt(LocalDateTime.now().minusMonths(1))
                .build();
        secondAnalyticsEvent = initAnalyticsEvent(SECOND_ANALYTICS_EVENT_ID, RECEIVER_ID, EVENT_TYPE)
                .receivedAt(LocalDateTime.now().minusMonths(2))
                .build();
        secondAnalyticsEventDto = initAnalyticsEventDto(SECOND_ANALYTICS_EVENT_ID, RECEIVER_ID, EVENT_TYPE)
                .receivedAt(LocalDateTime.now().minusMonths(2))
                .build();
    }

    @Test
    public void whenSaveThenGetAnalyticsEventDto() {
        when(analyticsEventMapper.toEntity(firstAnalyticsEventDto)).thenReturn(firstAnalyticsEvent);
        when(analyticsEventRepository.save(firstAnalyticsEvent)).thenReturn(firstAnalyticsEvent);
        when(analyticsEventMapper.toDto(firstAnalyticsEvent)).thenReturn(firstAnalyticsEventDto);
        AnalyticsEventDto actual = analyticsEventService.save(firstAnalyticsEventDto);
        assertThat(actual).isEqualTo(firstAnalyticsEventDto);
    }

    @Test
    public void whenGetAnalyticsAndIntervalNotNullThenGetListFilteredByInterval() {
        Interval interval = new Interval(TypeOfInterval.MONTH, 3);
        when(analyticsEventRepository.findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE)).thenReturn(
                Stream.of(firstAnalyticsEvent, secondAnalyticsEvent));
        when(analyticsEventMapper.toDto(firstAnalyticsEvent)).thenReturn(firstAnalyticsEventDto);
        when(analyticsEventMapper.toDto(secondAnalyticsEvent)).thenReturn(secondAnalyticsEventDto);
        List<AnalyticsEventDto> actual = analyticsEventService.getAnalytics(RECEIVER_ID, EVENT_TYPE, interval, FROM, TO);
        assertThat(actual).isEqualTo(List.of(secondAnalyticsEventDto, firstAnalyticsEventDto));
    }

    @Test
    public void whenGetAnalyticsAndIntervalIsNullThenGetListFilteredByFromAndTo() {
        when(analyticsEventRepository.findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE)).thenReturn(
                Stream.of(firstAnalyticsEvent, secondAnalyticsEvent));
        when(analyticsEventMapper.toDto(firstAnalyticsEvent)).thenReturn(firstAnalyticsEventDto);
        List<AnalyticsEventDto> actual = analyticsEventService.getAnalytics(RECEIVER_ID, EVENT_TYPE, null, FROM, TO);
        assertThat(actual).isEqualTo(List.of(firstAnalyticsEventDto));
    }


    private AnalyticsEvent.AnalyticsEventBuilder initAnalyticsEvent(long eventId, long receiverId, EventType eventType) {
        return AnalyticsEvent.builder()
                .id(eventId)
                .receiverId(receiverId)
                .eventType(eventType);
    }

    private AnalyticsEventDto.AnalyticsEventDtoBuilder initAnalyticsEventDto(long eventId, long receiverId, EventType eventType) {
        return AnalyticsEventDto.builder()
                .id(eventId)
                .receiverId(receiverId)
                .eventType(eventType);
    }
}