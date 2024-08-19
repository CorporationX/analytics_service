package faang.school.analytics.service;

import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validator.analyticsEvent.AnalyticsEventValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AnalyticsEventValidator analyticsEventValidator;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAnalyticsWithInterval() {
        Long receiverId = 1L;
        EventType eventType = EventType.ACHIEVEMENT_RECEIVED;
        Interval interval = Interval.LAST_7_DAYS;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = interval.getStartTime();
        LocalDateTime endTime = interval.getEndTime();

        AnalyticsEvent event = new AnalyticsEvent();
        event.setReceivedAt(now.minusDays(1));
        List<AnalyticsEvent> events = List.of(event);
        List<AnalyticsEventDto> dtos = List.of(new AnalyticsEventDto());

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(events.stream());
        when(analyticsEventMapper.toDto(any(AnalyticsEvent.class)))
                .thenReturn(new AnalyticsEventDto());

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(receiverId, eventType, interval, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(analyticsEventRepository).findByReceiverIdAndEventType(receiverId, eventType);
    }

    @Test
    public void testGetAnalyticsWithCustomDateRange() {
        Long receiverId = 1L;
        EventType eventType = EventType.ACHIEVEMENT_RECEIVED;
        LocalDateTime from = LocalDateTime.now().minusDays(2);
        LocalDateTime to = LocalDateTime.now().minusHours(1);

        AnalyticsEvent event = new AnalyticsEvent();
        event.setReceivedAt(LocalDateTime.now().minusDays(1));
        List<AnalyticsEvent> events = List.of(event);
        List<AnalyticsEventDto> dtos = List.of(new AnalyticsEventDto());

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(events.stream());
        when(analyticsEventMapper.toDto(any(AnalyticsEvent.class)))
                .thenReturn(new AnalyticsEventDto());

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(receiverId, eventType, null, from, to);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(analyticsEventRepository).findByReceiverIdAndEventType(receiverId, eventType);
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
