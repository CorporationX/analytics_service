package faang.school.analytics.service.analytics_event;

import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.dto.analytics_event.AnalyticsEventDto;
import faang.school.analytics.dto.analytics_event.AnalyticsEventGetDto;
import faang.school.analytics.exception.IntervalsNotValidException;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.TimeUnit;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    private static final long ANALYTICS_EVENT_ID_ONE = 1L;
    private static final long RECEIVER_ID_ONE = 1L;
    private static final long ACTOR_ID_ONE = 1L;
    private static final LocalDateTime SOME_TIME_AGO =
            LocalDateTime.of(2024, 10, 8, 13, 10, 10);
    private static final LocalDateTime FROM_DATE = LocalDateTime.of(2024, 10, 7, 13, 15);
    private static final LocalDateTime TO_DATE = LocalDateTime.of(2024, 10, 8, 13, 15);

    @InjectMocks
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private UserContext userContext;

    private AnalyticsEvent event;
    private AnalyticsEventDto eventDto;
    private AnalyticsEventGetDto analyticsEventGetDto;


    @BeforeEach
    void setUp() {

        event = AnalyticsEvent.builder()
                .id(ANALYTICS_EVENT_ID_ONE)
                .receiverId(RECEIVER_ID_ONE)
                .actorId(ACTOR_ID_ONE)
                .eventType(EventType.POST_LIKE)
                .receivedAt(SOME_TIME_AGO)
                .build();

        eventDto = AnalyticsEventDto.builder()
                .id(ANALYTICS_EVENT_ID_ONE)
                .receiverId(RECEIVER_ID_ONE)
                .actorId(ACTOR_ID_ONE)
                .eventType(EventType.POST_LIKE)
                .receivedAt(SOME_TIME_AGO)
                .build();

        analyticsEventGetDto = AnalyticsEventGetDto.builder()
                .eventType(EventType.POST_LIKE)
                .timeQuantity(2)
                .timeUnit(TimeUnit.MONTH)
                .from(FROM_DATE)
                .to(TO_DATE)
                .build();
    }

    @Test
    @DisplayName("Saves event in DB return eventDto")
    public void whenEventPassedThenSavesItInDBAndReturnDto() {
        when(analyticsEventRepository.save(event)).thenReturn(event);
        when(analyticsEventMapper.toAnalyticsEventDto(event)).thenReturn(eventDto);

        AnalyticsEventDto savedEvent = analyticsEventService.saveEvent(event);
        assertEquals(savedEvent.getId(), eventDto.getId());
        assertEquals(savedEvent.getEventType(), eventDto.getEventType());
    }

    @Test
    @DisplayName("Find events in DB specified by parameters passed in when interval is present" +
            " and return list of eventDtos by time")
    public void whenEventGetDtoPassedWithIntervalFindEventsDescribedInItThenReturnEventDtoListSortedByTime() {
        when(userContext.getUserId()).thenReturn(RECEIVER_ID_ONE);
        when(analyticsEventRepository
                .findByReceiverIdAndEventType(RECEIVER_ID_ONE, analyticsEventGetDto.getEventType()))
                .thenReturn(Stream.of(event));
        when(analyticsEventMapper.toAnalyticsEventDto(event)).thenReturn(eventDto);

        List<AnalyticsEventDto> resultList = analyticsEventService.getAnalytics(analyticsEventGetDto);

        assertEquals(1, resultList.size());
        assertEquals(event.getEventType(), resultList.get(0).getEventType());
    }

    @Test
    @DisplayName("Find events in DB specified by parameters passed in when timeUnit and quantity are null" +
            " and from and to dates are present then return list of eventDtos by time")
    public void whenEventGetDtoPassedWithFromToDatesFindEventsDescribedInItThenReturnEventDtoListSortedByTime() {
        analyticsEventGetDto.setTimeUnit(null);
        analyticsEventGetDto.setTimeQuantity(null);
        when(userContext.getUserId()).thenReturn(RECEIVER_ID_ONE);
        when(analyticsEventRepository
                .findByReceiverIdAndEventType(RECEIVER_ID_ONE, analyticsEventGetDto.getEventType()))
                .thenReturn(Stream.of(event));
        when(analyticsEventMapper.toAnalyticsEventDto(event)).thenReturn(eventDto);

        List<AnalyticsEventDto> resultList = analyticsEventService.getAnalytics(analyticsEventGetDto);

        assertEquals(1, resultList.size());
        assertEquals(event.getEventType(), resultList.get(0).getEventType());
    }

    @Test
    @DisplayName("If Interval and from and to dates are null then throw exception")
    public void whenEventGetDtoIntervalAndFromToDatesAreNullThenThrowException() {
        analyticsEventGetDto.setTimeUnit(null);
        analyticsEventGetDto.setTimeQuantity(null);
        analyticsEventGetDto.setFrom(null);
        when(userContext.getUserId()).thenReturn(RECEIVER_ID_ONE);
        when(analyticsEventRepository
                .findByReceiverIdAndEventType(RECEIVER_ID_ONE, analyticsEventGetDto.getEventType()))
                .thenReturn(Stream.of(event));

        assertThrows(IntervalsNotValidException.class, () ->
                analyticsEventService.getAnalytics(analyticsEventGetDto));
    }
}
