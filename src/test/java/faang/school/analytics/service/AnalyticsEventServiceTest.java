package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventResponseDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validator.AnalyticsEventValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;

import faang.school.analytics.dto.MentorshipRequestEvent;
import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class AnalyticsEventServiceTest {
    private static final int WEEKS_COUNT = 1;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private AnalyticsEventValidator analyticsEventValidator;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    private AnalyticsEvent analyticsEvent;
    private AnalyticsEventResponseDto responseDto;
    private Long receiverId;
    private EventType eventType;
    private Interval interval;
    private LocalDateTime from;
    private LocalDateTime to;
    private MentorshipRequestEvent mentorshipRequestEvent;
    private AnalyticsEvent analyticsEventMentorshipRequest;

    @BeforeEach
    void setUp() {
        receiverId = 2L;
        eventType = EventType.FOLLOWER;
        interval = Interval.WEEK;
        to = LocalDateTime.now();
        from = to.minusWeeks(WEEKS_COUNT);

        analyticsEvent = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(receiverId)
                .actorId(3L)
                .eventType(eventType)
                .build();

        responseDto = AnalyticsEventResponseDto.builder()
                .id(1L)
                .receiverId(receiverId)
                .eventType(eventType)
                .build();

        mentorshipRequestEvent = new MentorshipRequestEvent(1L, 2L, null);
        analyticsEventMentorshipRequest = new AnalyticsEvent();
    }

    @Test
    void testSaveEventSuccess() {
        when(analyticsEventRepository.save(analyticsEvent)).thenReturn(analyticsEvent);

        analyticsEventService.saveEvent(analyticsEvent);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    void testGetAnalyticsWithInterval() {
        when(analyticsEventRepository.findAll(
                ArgumentMatchers.<Specification<AnalyticsEvent>>any(),
                ArgumentMatchers.any(Sort.class))).thenReturn(List.of(analyticsEvent));
        when(analyticsEventMapper.entityToResponseDto(analyticsEvent)).thenReturn(responseDto);

        List<AnalyticsEventResponseDto> result = analyticsEventService.getAnalytics(
                receiverId, eventType, interval, from, to);

        assertThat(result).containsExactly(responseDto);
        verify(analyticsEventValidator, never()).validateDateOrder(from, to);
        verify(analyticsEventRepository, times(1)).findAll(
                ArgumentMatchers.<Specification<AnalyticsEvent>>any(),
                ArgumentMatchers.any(Sort.class));
        verify(analyticsEventMapper, times(1)).entityToResponseDto(analyticsEvent);
    }

    @Test
    void testGetAnalyticsWithFromAndTo() {
        interval = null;

        when(analyticsEventRepository.findAll(
                ArgumentMatchers.<Specification<AnalyticsEvent>>any(),
                ArgumentMatchers.any(Sort.class)))
                .thenReturn(List.of(analyticsEvent));
        when(analyticsEventMapper.entityToResponseDto(analyticsEvent)).thenReturn(responseDto);

        List<AnalyticsEventResponseDto> result = analyticsEventService.getAnalytics(
                receiverId, eventType, interval, from, to);

        assertWhenIntervalIsNull(result);
    }

    @Test
    void testGetAnalyticsWithFromOnly() {
        interval = null;
        to = null;

        when(analyticsEventRepository.findAll(
                ArgumentMatchers.<Specification<AnalyticsEvent>>any(),
                ArgumentMatchers.any(Sort.class)))
                .thenReturn(List.of(analyticsEvent));
        when(analyticsEventMapper.entityToResponseDto(analyticsEvent)).thenReturn(responseDto);

        List<AnalyticsEventResponseDto> result = analyticsEventService.getAnalytics(
                receiverId, eventType, interval, from, to);

        assertWhenIntervalIsNull(result);
    }

    @Test
    void testGetAnalyticsWithToOnly() {
        interval = null;
        from = null;

        when(analyticsEventRepository.findAll(
                ArgumentMatchers.<Specification<AnalyticsEvent>>any(),
                ArgumentMatchers.any(Sort.class)))
                .thenReturn(List.of(analyticsEvent));
        when(analyticsEventMapper.entityToResponseDto(analyticsEvent)).thenReturn(responseDto);

        List<AnalyticsEventResponseDto> result = analyticsEventService.getAnalytics(
                receiverId, eventType, interval, from, to);

        assertWhenIntervalIsNull(result);
    }

    @Test
    void testGetAnalyticsWithAllNull() {
        interval = null;
        from = null;
        to = null;

        when(analyticsEventRepository.findAll(
                ArgumentMatchers.<Specification<AnalyticsEvent>>any(),
                ArgumentMatchers.any(Sort.class))).thenReturn(List.of(analyticsEvent));
        when(analyticsEventMapper.entityToResponseDto(analyticsEvent)).thenReturn(responseDto);

        List<AnalyticsEventResponseDto> result = analyticsEventService.getAnalytics(
                receiverId, eventType, interval, from, to);

        assertWhenIntervalIsNull(result);
    }

    private void assertWhenIntervalIsNull(List<AnalyticsEventResponseDto> result) {
        assertThat(result).containsExactly(responseDto);
        verify(analyticsEventValidator, times(1)).validateDateOrder(from, to);
        verify(analyticsEventRepository, times(1)).findAll(
                ArgumentMatchers.<Specification<AnalyticsEvent>>any(),
                ArgumentMatchers.any(Sort.class));
        verify(analyticsEventMapper, times(1)).entityToResponseDto(analyticsEvent);
    }

    @Test
    void saveMentorshipRequestEventSuccessfully() {
        when(analyticsEventMapper.toAnalyticsEventMentorshipRequest(mentorshipRequestEvent))
                .thenReturn(analyticsEventMentorshipRequest);

        analyticsEventService.saveAnalyticsEvent(mentorshipRequestEvent);

        verify(analyticsEventMapper, times(1)).toAnalyticsEventMentorshipRequest(mentorshipRequestEvent);
        verify(analyticsEventRepository, times(1)).save(analyticsEventMentorshipRequest);
    }

    @Test
    void saveMentorshipRequestEvent_ShouldThrowExceptionWhenRepositoryFails() {
        when(analyticsEventMapper.toAnalyticsEventMentorshipRequest(mentorshipRequestEvent))
                .thenReturn(analyticsEventMentorshipRequest);
        doThrow(new RuntimeException("Database error")).when(analyticsEventRepository).save(analyticsEventMentorshipRequest);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            analyticsEventService.saveAnalyticsEvent(mentorshipRequestEvent);
        });

        assertEquals("Database error", exception.getMessage());
    }
}