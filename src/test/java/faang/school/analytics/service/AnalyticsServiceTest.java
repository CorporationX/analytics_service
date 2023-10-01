package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticEventDto;
import faang.school.analytics.dto.AnalyticsIntervalDto;
import faang.school.analytics.exeption.DataValidationException;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.analytics.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;
    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    public void getLatestEventsTest() {
        analyticsService.setLatestPeriodOfTime("1 day");

        analyticsService.getLatestEvents();

        Mockito.verify(analyticsEventRepository, Mockito.times(1)).getLatestEvents("1 day");
    }

    @Test
    public void testAnalyticsByInterval() {
        AnalyticsIntervalDto analyticsIntervalDto = AnalyticsIntervalDto.builder()
                .receiverId(1L)
                .eventType(EventType.FOLLOWER)
                .interval("1days")
                .build();
        List<AnalyticsEvent> analyticsEvents = Arrays.asList(
                new AnalyticsEvent(1L, 1L, 1L, EventType.FOLLOWER, LocalDateTime.now()),
                new AnalyticsEvent(2L, 1L, 1L, EventType.FOLLOWER, LocalDateTime.now())
        );
        List<AnalyticEventDto> analyticEventsDto = Arrays.asList(
                new AnalyticEventDto(1L, 1L, 1L, EventType.FOLLOWER, LocalDateTime.now()),
                new AnalyticEventDto(2L, 1L, 1L, EventType.FOLLOWER, LocalDateTime.now())
        );

        when(analyticsEventRepository.getEventsByInterval(any(LocalDateTime.class), eq(analyticsIntervalDto.getReceiverId()),
                eq(analyticsIntervalDto.getEventType().toString()))).thenReturn(analyticsEvents);

        List<AnalyticEventDto> result = analyticsService.getAnalytics(analyticsIntervalDto);

        assertEquals(result.size(), 2);
        assertEquals(result.get(1).getId(), analyticEventsDto.get(1).getId());
    }

    @Test
    public void testAnalyticsException() {
        AnalyticsIntervalDto analyticsIntervalDto = AnalyticsIntervalDto.builder()
                .receiverId(1L)
                .eventType(EventType.FOLLOWER)
                .build();

        assertThrows(DataValidationException.class, () -> analyticsService.getAnalytics(analyticsIntervalDto));
    }
}
