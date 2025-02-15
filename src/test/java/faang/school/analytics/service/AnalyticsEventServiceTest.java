package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    private AnalyticsEvent analyticsEvent;
    private AnalyticsEventDto analyticsEventDto;

    @BeforeEach
    void setUp() {
        analyticsEvent = new AnalyticsEvent();
        analyticsEventDto = new AnalyticsEventDto();
    }

    @Test
    void saveEvent_ShouldSaveAndReturnDto() {
        when(analyticsEventMapper.toDto(analyticsEvent)).thenReturn(analyticsEventDto);

        AnalyticsEventDto result = analyticsEventService.saveEvent(analyticsEvent);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
        verify(analyticsEventMapper, times(1)).toDto(analyticsEvent);
        assertEquals(analyticsEventDto, result);
    }

    @Test
    void getAnalytics_WithInterval_ShouldCallCorrectRepositoryMethod() {
        long receiverId = 1L;
        EventType eventType = EventType.PROFILE_VIEW;
        Interval interval = Interval.DAY;

        when(analyticsEventRepository.findByReceiverIdAndEventTypeAndAfterDate(eq(receiverId), eq(eventType), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(analyticsEvent));
        when(analyticsEventMapper.toDto(any())).thenReturn(analyticsEventDto);

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(receiverId, eventType, interval, null, null);

        verify(analyticsEventRepository, times(1))
                .findByReceiverIdAndEventTypeAndAfterDate(eq(receiverId), eq(eventType), any(LocalDateTime.class));
        verify(analyticsEventMapper, times(1)).toDto(analyticsEvent);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }


    @Test
    void getAnalytics_WithoutInterval_ShouldCallCorrectRepositoryMethod() {
        long receiverId = 1L;
        EventType eventType = EventType.PROFILE_VIEW;
        LocalDateTime from = LocalDateTime.now().minusDays(2);
        LocalDateTime to = LocalDateTime.now();

        when(analyticsEventRepository.findByReceiverIdAndEventTypeAndDateRange(receiverId, eventType, from, to))
                .thenReturn(Collections.singletonList(analyticsEvent));
        when(analyticsEventMapper.toDto(any())).thenReturn(analyticsEventDto);

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(receiverId, eventType, null, from, to);

        verify(analyticsEventRepository, times(1))
                .findByReceiverIdAndEventTypeAndDateRange(receiverId, eventType, from, to);
        verify(analyticsEventMapper, times(1)).toDto(analyticsEvent);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
