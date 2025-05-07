package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsRequestDto;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.enums.EventType;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.Interval;
import faang.school.analytics.parser.AnalyticsRequestParser;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Stream;

import static faang.school.analytics.constants.Constants.EVENT_NULL_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceImplTest {
    @Mock
    private AnalyticsEventRepository eventRepository;

    @Mock
    private AnalyticsRequestParser parser;

    @Spy
    private AnalyticsEventMapperImpl eventMapper = new AnalyticsEventMapperImpl();

    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    @Captor
    private ArgumentCaptor<AnalyticsEvent> analyticsEventCaptor;

    private Clock fixedClock;
    private final long receiverId = 123L;
    private final EventType eventType = EventType.PROJECT_VIEW;
    private final Instant start = Instant.parse("2025-01-01T00:00:00Z");
    private final Instant end = Instant.parse("2025-12-31T23:59:59Z");
    private final Instant fixedNow = LocalDateTime.of(2025, 5, 2, 22, 0)
            .atZone(ZoneId.systemDefault()).toInstant();

    private AnalyticsEvent eventLastHour;
    private AnalyticsEvent eventLastDay;
    private AnalyticsEvent eventLastWeek;
    private AnalyticsEvent eventLastMonth;

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(fixedNow, ZoneId.systemDefault());
        analyticsEventService = new AnalyticsEventServiceImpl(eventMapper, eventRepository, parser, fixedClock);

        LocalDateTime fixedDateTime = LocalDateTime.ofInstant(fixedNow, ZoneId.systemDefault());
        eventLastHour = AnalyticsEvent.builder().receivedAt(fixedDateTime.minusMinutes(59)).build();
        eventLastDay = AnalyticsEvent.builder().receivedAt(fixedDateTime.minusHours(23)).build();
        eventLastWeek = AnalyticsEvent.builder().receivedAt(fixedDateTime.minusDays(5)).build();
        eventLastMonth = AnalyticsEvent.builder().receivedAt(fixedDateTime.minusDays(20)).build();
    }

    @Test
    public void testSaveCommentEvent() {
        CommentEvent commentEvent = new CommentEvent();

        analyticsEventService.saveCommentEvent(commentEvent);

        verify(eventRepository, times(1)).save(analyticsEventCaptor.capture());
        assertEquals(EventType.POST_COMMENT, analyticsEventCaptor.getValue().getEventType());
    }

    @Test
    public void testSaveEventThrowsWhenNull() {
        assertThatThrownBy(() -> analyticsEventService.saveEvent(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(EVENT_NULL_EXCEPTION);
    }

    @Test
    public void testSaveEventSuccessfully() {
        AnalyticsEvent event = new AnalyticsEvent();
        analyticsEventService.saveEvent(event);
        verify(eventRepository).save(event);
    }

    @Test
    public void testGetParseAnalytics_returnsOnlyEventsWithDateRange() {
        AnalyticsRequestDto dto = AnalyticsRequestDto.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .startDate(start)
                .endDate(end)
                .build();

        LocalDateTime withinTime = LocalDateTime.ofInstant(Instant.parse("2025-06-01T12:00:00Z"), ZoneId.systemDefault());
        AnalyticsEvent within = AnalyticsEvent.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .receivedAt(withinTime)
                .build();
        AnalyticsEvent before = AnalyticsEvent.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .receivedAt(LocalDateTime.ofInstant(start.minusSeconds(1), ZoneId.systemDefault()))
                .build();
        AnalyticsEvent after = AnalyticsEvent.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .receivedAt(LocalDateTime.ofInstant(end.plusSeconds(1), ZoneId.systemDefault()))
                .build();
        when(eventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(within, before, after));

        List<AnalyticsEvent> result = analyticsEventService.getParseAnalytics(dto);

        assertThat(result).containsExactly(within);
    }

    @Test
    public void testGetParseAnalytics_returnsEmptyListWhenNoEventsInRange() {
        AnalyticsRequestDto dto = AnalyticsRequestDto.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .startDate(start)
                .endDate(end)
                .build();
        when(eventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.empty());

        List<AnalyticsEvent> result = analyticsEventService.getParseAnalytics(dto);
        assertThat(result).isEmpty();
    }

    @Test
    public void testGetParseAnalytics_returnsMultipleEventsWithinRange() {
        AnalyticsRequestDto dto = AnalyticsRequestDto.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .startDate(start)
                .endDate(end)
                .build();
        LocalDateTime anotherTime = LocalDateTime.of(2025, 3, 15, 10, 0);
        AnalyticsEvent another = AnalyticsEvent.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .receivedAt(anotherTime)
                .build();
        LocalDateTime withinTime = LocalDateTime.ofInstant(Instant.parse("2025-06-01T12:00:00Z"), ZoneId.systemDefault());
        AnalyticsEvent within = AnalyticsEvent.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .receivedAt(withinTime)
                .build();
        when(eventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(within, another));

        List<AnalyticsEvent> result = analyticsEventService.getParseAnalytics(dto);
        assertThat(result).containsExactlyInAnyOrder(within, another);
    }

    @Test
    public void testGetParseAnalytics_throwsWhenNoIntervalAndNoDates() {
        AnalyticsRequestDto dto = AnalyticsRequestDto.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .build();
        assertThatThrownBy(() -> analyticsEventService.getParseAnalytics(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(AnalyticsEventServiceImpl.FROM_OR_TO_NULL_EXCEPTION);
    }

    @Test
    public void testGetParseAnalytics_withInterval() {
        AnalyticsRequestDto dto = AnalyticsRequestDto.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .interval(Interval.LAST_MONTH)
                .build();

        Instant intervalStart = Interval.LAST_MONTH.getStartDate(fixedClock);
        LocalDateTime eventTime = LocalDateTime.ofInstant(intervalStart.plusSeconds(3600), ZoneId.systemDefault());
        AnalyticsEvent event = AnalyticsEvent.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .receivedAt(eventTime)
                .build();
        when(eventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(event));

        List<AnalyticsEvent> result = analyticsEventService.getParseAnalytics(dto);
        assertThat(result).containsExactly(event);
    }

    @Test
    public void testGetAnalytics_throwsWhenEventTypeIsNull() {
        assertThatThrownBy(() -> analyticsEventService.getAnalytics(
                receiverId, null, null, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(AnalyticsEventServiceImpl.EVENT_TYPE_NULL_EXCEPTION);
    }

    @Test
    public void testGetAnalytics_throwsWhenFromDateNull() {
        assertThatThrownBy(() -> analyticsEventService.getAnalytics(
                receiverId, EventType.FOLLOWER, null, null, Instant.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(AnalyticsEventServiceImpl.FROM_OR_TO_NULL_EXCEPTION);
    }

    @Test
    public void testGetAnalytics_throwsWhenToDateNull() {
        assertThatThrownBy(() -> analyticsEventService.getAnalytics(
                receiverId, EventType.FOLLOWER, null, Instant.now(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(AnalyticsEventServiceImpl.FROM_OR_TO_NULL_EXCEPTION);
    }

    @Test
    public void testGetAnalytics_filtersByLastHourInterval() {
        when(eventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(eventLastHour, eventLastDay));
        when(eventMapper.toAnalyticsEventDtoList(any())).thenCallRealMethod();

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(
                receiverId, EventType.FOLLOWER, Interval.LAST_HOUR, null, null);
        assertThat(result).extracting(AnalyticsEventDto::getReceivedAt)
                .containsExactly(eventLastHour.getReceivedAt());
    }

    @Test
    public void testGetAnalytics_filtersByLastDayInterval() {
        when(eventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(eventLastHour, eventLastDay));
        when(eventMapper.toAnalyticsEventDtoList(any())).thenCallRealMethod();

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(
                receiverId, EventType.FOLLOWER, Interval.YESTERDAY, null, null);
        assertThat(result).extracting(AnalyticsEventDto::getReceivedAt)
                .containsExactly(eventLastHour.getReceivedAt(), eventLastDay.getReceivedAt());
    }

    @Test
    public void testGetAnalytics_filtersByLastWeekInterval() {
        when(eventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(eventLastWeek, eventLastMonth));
        when(eventMapper.toAnalyticsEventDtoList(any())).thenCallRealMethod();

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(
                receiverId, EventType.FOLLOWER, Interval.LAST_WEEK, null, null);
        assertThat(result).extracting(AnalyticsEventDto::getReceivedAt)
                .containsExactly(eventLastWeek.getReceivedAt());
    }

    @Test
    public void testGetAnalytics_filtersByLastMonthInterval() {
        when(eventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(eventLastMonth, eventLastWeek));
        when(eventMapper.toAnalyticsEventDtoList(any())).thenCallRealMethod();

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(
                receiverId, EventType.FOLLOWER, Interval.LAST_MONTH, null, null);
        assertThat(result).extracting(AnalyticsEventDto::getReceivedAt)
                .containsExactly(eventLastWeek.getReceivedAt(), eventLastMonth.getReceivedAt());
    }

    @Test
    public void testGetAnalyticsByDateFromAndToWithIntervalNull() {
        LocalDateTime fromLT = LocalDateTime.now().minusDays(3);
        LocalDateTime toLT = LocalDateTime.now().minusDays(1);
        Instant from = fromLT.atZone(ZoneId.systemDefault()).toInstant();
        Instant to = toLT.atZone(ZoneId.systemDefault()).toInstant();

        LocalDateTime inRangeLT = LocalDateTime.now().minusDays(2);
        LocalDateTime outOfRangeLT = LocalDateTime.now().minusDays(5);
        AnalyticsEvent eventInRange = AnalyticsEvent.builder().receivedAt(inRangeLT).build();
        AnalyticsEvent eventOutOfRange = AnalyticsEvent.builder().receivedAt(outOfRangeLT).build();
        when(eventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(eventInRange, eventOutOfRange));

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(
                receiverId, EventType.FOLLOWER, null, from, to);
        assertThat(result).extracting(AnalyticsEventDto::getReceivedAt)
                .containsExactly(eventInRange.getReceivedAt());
    }
}
