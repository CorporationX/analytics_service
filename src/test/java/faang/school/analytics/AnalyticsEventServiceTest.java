package faang.school.analytics;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.filter.AnalyticsEventFilter;
import faang.school.analytics.filter.AnalyticsEventIntervalFilter;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    private List<AnalyticsEventFilter> analyticsEventFilters;

    @BeforeEach
    void setUp() {
        analyticsEventFilters = new ArrayList<>();
        analyticsEventFilters.add(mock(AnalyticsEventIntervalFilter.class));
        analyticsEventService = new AnalyticsEventService(analyticsEventRepository, analyticsEventMapper, analyticsEventFilters);
    }

    @Test
    public void saveEventShouldReturnSavedEventDto() {
        AnalyticsEvent event = new AnalyticsEvent();
        AnalyticsEventDto eventDto = new AnalyticsEventDto();
        when(analyticsEventRepository.save(any(AnalyticsEvent.class))).thenReturn(event);
        when(analyticsEventMapper.analyticsEventToAnalyticsEventDto(event)).thenReturn(eventDto);

        AnalyticsEventDto result = analyticsEventService.saveEvent(event);

        assertEquals(eventDto, result);
        verify(analyticsEventRepository).save(event);
        verify(analyticsEventMapper).analyticsEventToAnalyticsEventDto(event);
    }

    @Test
    public void getAnalyticsShouldReturnFilteredAnalyticsEventDtoList() {
        long receiverId = 1L;
        EventType eventType = EventType.FOLLOWER;
        AnalyticsEventFilterDto filterDto = new AnalyticsEventFilterDto();
        filterDto.setReceiverId(receiverId);
        filterDto.setEventType(eventType);

        List<AnalyticsEvent> events = Arrays.asList(new AnalyticsEvent(), new AnalyticsEvent());
        Stream<AnalyticsEvent> eventStream = events.stream();
        when(analyticsEventRepository.findByReceiverIdAndEventTypeOrderByReceiverIdDesc(receiverId, eventType))
                .thenReturn(eventStream);

        AnalyticsEventFilter filter = mock(AnalyticsEventFilter.class);
        analyticsEventFilters.add(filter);

        when(filter.isApplicable(filterDto)).thenReturn(true);
        when(filter.apply(any(Stream.class), eq(filterDto))).thenAnswer(invocation -> {
            Stream<AnalyticsEvent> stream = invocation.getArgument(0);
            return stream.filter(event -> true);
        });

        List<AnalyticsEventDto> eventDtos = Arrays.asList(new AnalyticsEventDto(), new AnalyticsEventDto());
        when(analyticsEventMapper.analyticsEventListToAnalyticsEventDtoList(anyList()))
                .thenReturn(eventDtos);

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(filterDto);

        assertEquals(eventDtos, result);
        verify(analyticsEventRepository).findByReceiverIdAndEventTypeOrderByReceiverIdDesc(receiverId, eventType);
        verify(filter).isApplicable(filterDto);
        verify(filter).apply(any(Stream.class), eq(filterDto));
        verify(analyticsEventMapper).analyticsEventListToAnalyticsEventDtoList(anyList());
    }
}
