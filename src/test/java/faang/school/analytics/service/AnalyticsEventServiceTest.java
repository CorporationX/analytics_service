package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.event.ProfileViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private ProfileViewEvent event;
    private AnalyticsEvent expectedAnalyticsEvent;

    @BeforeEach
    void setUp() {
        analyticsEvent = new AnalyticsEvent();
        analyticsEventDto = new AnalyticsEventDto();

        event = ProfileViewEvent.builder()
                .actorId(1L)
                .receivedAt(LocalDateTime.now())
                .receiverId(2L)
                .build();

        expectedAnalyticsEvent = AnalyticsEvent.builder()
                .actorId(1L)
                .receivedAt(LocalDateTime.now())
                .receiverId(2L)
                .build();

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
        LocalDateTime now = LocalDateTime.now();
        long receiverId = 1L;
        EventType eventType = EventType.PROFILE_VIEW;
        LocalDateTime from = now.minusDays(2);
        LocalDateTime to = now;

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

    @Test
    public void testCreateProfileViewEventSuccess() {
        when(analyticsEventMapper.toAnalyticsFromUserProfileView(event)).thenReturn(expectedAnalyticsEvent);
        analyticsEventService.createProfileViewEvent(event);
        Mockito.verify(analyticsEventMapper, times(1)).toAnalyticsFromUserProfileView(event);
        Mockito.verify(analyticsEventRepository, times(1)).save(expectedAnalyticsEvent);
    }

    @Test
    public void testCreateProfileViewEventWithNullEvent() {
        NullPointerException npe = Assertions.assertThrows(NullPointerException.class,
                () -> analyticsEventService.createProfileViewEvent(null));
        Assertions.assertEquals(npe.getMessage(), "Event cannot be null");

    }

}
