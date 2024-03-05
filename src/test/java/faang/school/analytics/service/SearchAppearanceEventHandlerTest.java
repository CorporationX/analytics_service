package faang.school.analytics.service;

import faang.school.analytics.event.SearchAppearanceEvent;
import faang.school.analytics.handler.SearchAppearanceEventHandler;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class SearchAppearanceEventHandlerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @InjectMocks
    private SearchAppearanceEventHandler searchAppearanceEventHandler;

    @Test
    public void testOnApplicationEventSaveAnalyticsEvent() {
        SearchAppearanceEvent searchAppearanceEvent = new SearchAppearanceEvent(1L, 2L, LocalDateTime.now());
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        analyticsEvent.setReceiverId(1L);
        analyticsEvent.setActorId(2L);
        analyticsEvent.setReceivedAt(LocalDateTime.now());

        Mockito.when(analyticsEventMapper.toAnalyticsEvent(searchAppearanceEvent)).thenReturn(analyticsEvent);

        searchAppearanceEventHandler.onApplicationEvent(searchAppearanceEvent);

        Mockito.verify(analyticsEventService, Mockito.times(1)).save(analyticsEvent);
    }
}