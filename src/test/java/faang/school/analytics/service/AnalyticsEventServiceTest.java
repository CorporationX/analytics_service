package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventRepository repository;
    @Spy
    private AnalyticsEventMapperImpl mapper;

    @InjectMocks
    private AnalyticsEventService service;

    @Test
    public void saveTest() {
        AnalyticsEvent event = new AnalyticsEvent();
        when(repository.save(event)).thenReturn(event);
        assertEquals(mapper.toDto(event), service.saveEvent(event));
    }

    @Test
    public void nullIntervalGetTest() {
        AnalyticsEvent event = new AnalyticsEvent(1, 1, 1, EventType.FOLLOWER, LocalDateTime.now().minusMonths(1));
        when(repository.findByReceiverIdAndEventType(1, EventType.FOLLOWER))
                .thenReturn(
                        Stream.of(event)
                );
        List<AnalyticsEventDto> result = service.getAnalytics(1, EventType.FOLLOWER, null, LocalDateTime.now().minusMonths(2), LocalDateTime.now());
        assertEquals(List.of(mapper.toDto(event)), result);
    }

    @Test
    public void intervalGetTest() {
        AnalyticsEvent event = new AnalyticsEvent(1, 1, 1, EventType.FOLLOWER, LocalDateTime.now().minusMonths(1));
        when(repository.findByReceiverIdAndEventType(1, EventType.FOLLOWER))
                .thenReturn(
                        Stream.of(event)
                );
        List<AnalyticsEventDto> result = service
                .getAnalytics(1, EventType.FOLLOWER, Interval.TWO_MONTHS_AGO, LocalDateTime.now().minusMonths(2), LocalDateTime.now());
        assertEquals(List.of(mapper.toDto(event)), result);
    }
}
