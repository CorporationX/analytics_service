package faang.school.analytics;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.services.AnalyticsEventService;
import faang.school.analytics.services.utils.AnalyticsUtilService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestAnalyticsEventService {
    @Mock
    private AnalyticsUtilService analyticsUtilService;
    @Mock
    private AnalyticsEventMapperImpl analyticsEventMapper;
    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    public void testSaveEvent() {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        analyticsEventService.saveEvent(analyticsEvent);
        verify(analyticsUtilService).saveEvent(analyticsEvent);
    }

    @Test
    public void testGetAnalytics() {
        long receiverId = 1L;
        EventType eventType = EventType.POST_LIKE;
        LocalDateTime from = LocalDateTime.of(2024, Month.APRIL, 15, 14, 20);
        LocalDateTime to = LocalDateTime.of(2024, Month.AUGUST, 27, 19, 26);
        AnalyticsEvent firstEvent = new AnalyticsEvent();
        AnalyticsEvent secondEvent = new AnalyticsEvent();
        AnalyticsEvent thirdEvent = new AnalyticsEvent();
        firstEvent.setReceivedAt(LocalDateTime.of(2024, Month.MAY, 17, 9, 30));
        secondEvent.setReceivedAt(LocalDateTime.of(2024, Month.JULY, 13, 14, 42));
        thirdEvent.setReceivedAt(LocalDateTime.of(2024, Month.SEPTEMBER, 21, 18, 55));
        when(analyticsUtilService.getAnalytics(receiverId, eventType)).thenReturn(Stream.of(firstEvent, secondEvent, thirdEvent));
        List<AnalyticsEventDto> resultList = analyticsEventService.getAnalytics(receiverId, eventType, null, from, to);
        assertEquals(2, resultList.size());
    }
}
