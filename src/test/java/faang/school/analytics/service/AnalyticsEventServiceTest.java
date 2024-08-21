package faang.school.analytics.service;

import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.dto.event.AnalyticsFilterDto;
import faang.school.analytics.dto.event.SortField;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.repository.filter.AnalyticsFilterRepository;
import faang.school.analytics.repository.filter.TimeRangeFilterSpecification;
import faang.school.analytics.validator.analyticsEvent.AnalyticsEventValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AnalyticsEventValidator analyticsEventValidator;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private UserContext userContext;

    private AnalyticsFilterRepository timeRangeFilter;

    private AnalyticsEventService analyticsEventService;

    private AnalyticsEvent analyticsEvent;
    private AnalyticsEventDto analyticsEventDto;
    private AnalyticsFilterDto filter;
    private List<AnalyticsFilterRepository> analyticsFilterRepositories;

    @BeforeEach
    public void init() {
        //MockitoAnnotations.openMocks(this);
        Interval interval = Interval.LAST_7_DAYS;
        filter = AnalyticsFilterDto.builder()
                .receiverId(1L)
                .from(interval.getStartTime())
                .to(interval.getEndTime())
                .page(0)
                .size(10)
                .sortField(SortField.RECEIVED_AT)
                .direction(Sort.Direction.ASC)
                .build();

        analyticsEvent = new AnalyticsEvent();
        analyticsEventDto = new AnalyticsEventDto();

        timeRangeFilter = new TimeRangeFilterSpecification();
        analyticsFilterRepositories = Arrays.asList(timeRangeFilter);

        analyticsEventService = new AnalyticsEventService(analyticsEventRepository, analyticsEventValidator, analyticsEventMapper, userContext, analyticsFilterRepositories);
    }

    @Test
    public void testGetAnalyticsWithInterval() {
        Page<AnalyticsEvent> expectedEventPage = new PageImpl<>(Collections.singletonList(analyticsEvent));

        when(analyticsEventRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedEventPage);

        Page<AnalyticsEventDto> actual = analyticsEventService.getAnalytics(filter);

        assertNotNull(actual);
        assertEquals(1, actual.getContent().size());
        verify(analyticsEventRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    public void testSaveEvent() {
        AnalyticsEventDto dto = new AnalyticsEventDto();
        dto.setId(1L);
        dto.setReceiverId(1L);
        dto.setActorId(2L);
        dto.setReceivedAt(LocalDateTime.now());

        AnalyticsEvent event = new AnalyticsEvent();
        when(analyticsEventMapper.toEntity(dto)).thenReturn(event);
        when(analyticsEventRepository.save(event)).thenReturn(event);
        when(analyticsEventMapper.toDto(event)).thenReturn(dto);

        AnalyticsEventDto result = analyticsEventService.saveEvent(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        verify(analyticsEventRepository).save(event);
    }

    @Test
    public void testPrepareAnalyticsToSave() {
        AnalyticsEventDto dto = new AnalyticsEventDto();
        dto.setActorId(null);
        dto.setReceivedAt(null);

        when(userContext.getUserId()).thenReturn(1L);

        AnalyticsEventDto result = analyticsEventService.prepareAnalyticsToSave(dto);

        assertNotNull(result);
        assertEquals(1L, result.getActorId());
        assertNotNull(result.getReceivedAt());
    }
}
