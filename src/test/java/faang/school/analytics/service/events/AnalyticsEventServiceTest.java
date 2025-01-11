package faang.school.analytics.service.events;

import faang.school.analytics.client.user.UserServiceClient;
import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.domain.dto.events.analytic.AnalyticsEventDto;
import faang.school.analytics.domain.dto.events.analytic.AnalyticsEventFilterDto;
import faang.school.analytics.domain.enums.Interval;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.mapper.events.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.analytic.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private AnalyticsEventFilter analyticsEventFilter;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private UserContext userContext;
    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    private AnalyticsEventDto analyticsEventDto;
    private final AnalyticsEventDto analyticsEventDtoWithWrongEventTypeNumber =
            AnalyticsEventDto.builder()
                    .eventTypeNumber(-1)
                    .build();

    @BeforeEach
    void setUp() {
        analyticsEventDto = AnalyticsEventDto.builder()
                .actorId(1L)
                .receiverId(2L)
                .eventTypeNumber(EventType.FOLLOWER.ordinal())
                .receivedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testSaveEventWithId() {
        AnalyticsEventDto dto = provideEventDto(1L, LocalDateTime.now());
        when(analyticsEventRepository.save(any()))
                .thenReturn(AnalyticsEvent.builder().build());
        analyticsEventService.saveEvent(dto);
    }

    @Test
    void testSaveEvent() {
        Mockito.when(analyticsEventRepository.save(any())).thenReturn(new AnalyticsEvent());
        Mockito.when(analyticsEventMapper.toEntity(any())).thenReturn(provideEvent(1L, LocalDateTime.now()));
        Mockito.when(analyticsEventMapper.toDto(any())).thenReturn(provideEventDto(1L, LocalDateTime.now()));

        AnalyticsEventDto dto = provideEventDto(null, LocalDateTime.now());

        assertDoesNotThrow(() -> analyticsEventService.saveEvent(dto));
        Mockito.verify(analyticsEventMapper, times(1)).toEntity(any());
        Mockito.verify(analyticsEventRepository, times(1)).save(any());
        Mockito.verify(analyticsEventMapper, times(1)).toDto(any());
    }

    @Test
    void testGetEventWithIncorrectFilters() {
        AnalyticsEventFilterDto filterDto = AnalyticsEventFilterDto.builder()
                .receiverId(1L)
                .eventType(EventType.FOLLOWER)
                .interval(Interval.DAY_1)
                .to(LocalDateTime.now())
                .build();
        assertThrows(DataValidationException.class, () -> analyticsEventService.getAnalytics(filterDto));
    }

    @Test
    void testGetEventsWithoutFilter() {
        Mockito.when(analyticsEventMapper.toDto(any())).thenReturn(provideEventDto(1L, LocalDateTime.now()));

        Stream<AnalyticsEvent> events = Stream.of(provideEvent(1L, LocalDateTime.now()), provideEvent(2L, LocalDateTime.now()));
        Mockito.when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any())).thenReturn(events);


        AnalyticsEventFilterDto filter = AnalyticsEventFilterDto.builder()
                .receiverId(1L)
                .eventType(EventType.FOLLOWER)
                .build();

        List<AnalyticsEventDto> res = analyticsEventService.getAnalytics(filter);

        assertEquals(2, res.size());
        Mockito.verify(analyticsEventMapper, times(2)).toDto(any());
        Mockito.verify(analyticsEventFilter, times(0)).filterByDates(any(), any(), any());
        Mockito.verify(analyticsEventFilter, times(0)).filterByInterval(any(), anyInt());
    }

    @Test
    void testGetEventsWithFilterByInterval() {
        Mockito.when(analyticsEventMapper.toDto(any())).thenReturn(provideEventDto(1L, LocalDateTime.now()));

        Stream<AnalyticsEvent> events = Stream.of(provideEvent(1L, LocalDateTime.now()), provideEvent(2L, LocalDateTime.now()));
        Mockito.when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any())).thenReturn(events);
        Mockito.when(analyticsEventFilter.filterByInterval(any(), anyInt())).thenReturn(events);


        AnalyticsEventFilterDto filter = AnalyticsEventFilterDto.builder()
                .receiverId(1L)
                .interval(Interval.DAY_1)
                .eventType(EventType.FOLLOWER)
                .build();

        List<AnalyticsEventDto> res = analyticsEventService.getAnalytics(filter);

        assertEquals(2, res.size());
        Mockito.verify(analyticsEventMapper, times(2)).toDto(any());
        Mockito.verify(analyticsEventFilter, times(0)).filterByDates(any(), any(), any());
        Mockito.verify(analyticsEventFilter, times(1)).filterByInterval(any(), anyInt());
    }

    @Test
    void testGetEventsWithFilterByDates() {
        Mockito.when(analyticsEventMapper.toDto(any())).thenReturn(provideEventDto(1L, LocalDateTime.now()));

        Stream<AnalyticsEvent> events = Stream.of(provideEvent(1L, LocalDateTime.now()), provideEvent(2L, LocalDateTime.now()));
        Mockito.when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any())).thenReturn(events);
        Mockito.when(analyticsEventFilter.filterByDates(any(), any(), any())).thenReturn(events);


        AnalyticsEventFilterDto filter = AnalyticsEventFilterDto.builder()
                .receiverId(1L)
                .to(LocalDateTime.now())
                .eventType(EventType.FOLLOWER)
                .build();

        List<AnalyticsEventDto> res = analyticsEventService.getAnalytics(filter);

        assertEquals(2, res.size());
        Mockito.verify(analyticsEventMapper, times(2)).toDto(any());
        Mockito.verify(analyticsEventFilter, times(1)).filterByDates(any(), any(), any());
        Mockito.verify(analyticsEventFilter, times(0)).filterByInterval(any(), anyInt());
    }

    @Test
    void getSumOfUsersActionsByEventTypeTest_ReturnTen() {
        int res = analyticsEventService.getSumOfUsersActionsByEventType(List.of(1, 2, 3, 4));

        assertEquals(10, res);
    }

    @Test
    void getSumOfUsersActionsByEventTypeTest_ReturnZero() {
        int res = analyticsEventService.getSumOfUsersActionsByEventType(List.of());

        assertEquals(0, res);
    }

    private AnalyticsEventDto provideEventDto(Long id, LocalDateTime date) {
        return AnalyticsEventDto.builder()
                .id(id)
                .eventTypeNumber(1)
                .receiverId(1L)
                .receivedAt(date)
                .actorId(1L)
                .build();
    }

    private AnalyticsEvent provideEvent(Long id, LocalDateTime dateTime) {
        return AnalyticsEvent.builder()
                .id(id)
                .receiverId(1)
                .eventType(EventType.of(1))
                .receivedAt(dateTime)
                .actorId(1)
                .build();
    }

}
