package faang.school.analytics.service.event;

import faang.school.analytics.dto.event.EventDto;
import faang.school.analytics.dto.event.Interval;
import faang.school.analytics.mapper.event.EventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class EventParamServiceTest {

    @Mock
    private AnalyticsEventService eventService;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventParamService eventParamService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEventsDto_ValidInput() {
        long receiverId = 1L;
        String eventTypeStr = "FOLLOWER";
        String intervalStr = "DAY";
        String fromStr = "01-01-2023 00:00:00";
        String toStr = "02-01-2023 00:00:00";

        EventDto eventDto = new EventDto();
        List<AnalyticsEvent> eventEntities = new ArrayList<>();
        eventEntities.add(new AnalyticsEvent());

        when(eventService.getEventsEntity(receiverId, EventType.FOLLOWER, Interval.DAY,
                LocalDateTime.parse(fromStr, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                LocalDateTime.parse(toStr, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))))
                .thenReturn(eventEntities);
        when(eventMapper.toDto(any(AnalyticsEvent.class))).thenReturn(eventDto);

        List<EventDto> result = eventParamService.getEventsDto(receiverId, eventTypeStr, intervalStr, fromStr, toStr);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(eventDto, result.get(0));
    }

    @Test
    void testGetEventsDto_InvalidEventType() {
        String invalidEventType = "INVALID_EVENT";

        assertThrows(IllegalArgumentException.class, () -> {
            eventParamService.getEventsDto(1L, invalidEventType, null, null, null);
        });

    }

    @Test
    void testGetEventsDto_InvalidInterval() {
        String invalidInterval = "INVALID_INTERVAL";

        assertThrows(IllegalArgumentException.class, () -> {
            eventParamService.getEventsDto(1L, "FOLLOWER", invalidInterval, null, null);
        });
    }

    @Test
    void testGetEventsDto_InvalidDateTimeFormat() {
        String invalidDateTime = "invalid-date-time";

        assertThrows(IllegalArgumentException.class, () -> {
            eventParamService.getEventsDto(1L, "FOLLOWER", "DAY", invalidDateTime, null);
        });
    }

    @Test
    void testGetEventsDto_NullEventType() {
        assertThrows(IllegalArgumentException.class, () -> {
            eventParamService.getEventsDto(1L, null, "DAY", null, null);
        });

    }
}