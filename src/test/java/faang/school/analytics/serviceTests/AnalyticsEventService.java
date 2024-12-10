package faang.school.analytics.serviceTests;

import faang.school.analytics.event.SearchAppearanceEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventMapper mapper;

    @InjectMocks
    private AnalyticsEventService service;

    @Test
    void processEvent() {
        MockitoAnnotations.openMocks(this);

        SearchAppearanceEvent event = new SearchAppearanceEvent();
        event.setUserId(1L);
        event.setSearchingUserId(2L);
        event.setViewedAt(LocalDateTime.now());

        String mockLog = "Mock log message";
        when(mapper.mapToLog(event)).thenReturn(mockLog);

        service.processEvent(event);

        verify(mapper, times(1)).mapToLog(event);
    }
}