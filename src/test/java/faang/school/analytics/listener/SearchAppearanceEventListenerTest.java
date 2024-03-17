package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.SearchAppearanceEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchAppearanceEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private SearchAppearanceEventListener searchAppearanceEventListener;

    @Test
    public void testHandleEventWhenSearchAppearanceEventThenSaveAnalyticsEvent() {
        SearchAppearanceEvent searchAppearanceEvent = new SearchAppearanceEvent(1L, 2L, LocalDateTime.now());
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        when(analyticsEventMapper.toAnalyticsEvent(searchAppearanceEvent)).thenReturn(analyticsEvent);

        searchAppearanceEventListener.handleEvent(searchAppearanceEvent);

        verify(analyticsEventService).save(analyticsEvent);
    }
}