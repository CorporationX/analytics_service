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
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceImplTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    private AnalyticsEventDto analyticsEventDto;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() {
        analyticsEventDto = AnalyticsEventDto.builder()
                .build();
        analyticsEvent = new AnalyticsEvent();
    }

    @Test
    void saveEvent_shouldSaveEvent() {

        when(analyticsEventMapper.toAnalyticsEvent(analyticsEventDto)).thenReturn(analyticsEvent);
        analyticsEventService.saveEvent(analyticsEventDto);
        verify(analyticsEventRepository).save(analyticsEvent);
    }

    @Test
    void getAnalytics_shouldReturnFilteredEvents() {
        long receiverId = 1L;
        EventType eventType = EventType.TASK_COMPLETED;
        Interval interval = Interval.DAY;
        LocalDateTime from = LocalDateTime.now().minusDays(2);
        LocalDateTime to = LocalDateTime.now();

        analyticsEvent.setReceivedAt(LocalDateTime.now().minusHours(1));

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(analyticsEvent));
        when(analyticsEventMapper.toAnalyticsEventDto(analyticsEvent))
                .thenReturn(analyticsEventDto);

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(receiverId, eventType, interval, from, to);

        assertEquals(1, result.size());
        verify(analyticsEventMapper).toAnalyticsEventDto(analyticsEvent);
    }
}

