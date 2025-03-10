package faang.school.analytics;

import faang.school.analytics.dto.SearchAppearanceEvent;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchAppearanceAnalyticsTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;
    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;
    private AnalyticsEvent analyticsEvent;
    private SearchAppearanceEvent searchAppearanceEvent;

    @BeforeEach
    public void setUp() {
        LocalDateTime now = LocalDateTime.now();
        analyticsEvent = AnalyticsEvent.builder()
                .receiverId(2L)
                .actorId(2L)
                .eventType(EventType.PROFILE_APPEARED_IN_SEARCH)
                .receivedAt(now)
                .build();
        searchAppearanceEvent = new SearchAppearanceEvent();
        searchAppearanceEvent.setFoundUserId(2L);
        searchAppearanceEvent.setUserId(2L);
        searchAppearanceEvent.setTime(now);
    }

    @Test
    public void testSaveSearchAppearanceEventSuccess() {
        when(analyticsEventRepository.save(analyticsEvent)).thenReturn(analyticsEvent);

        analyticsEventService.saveSearchAppearanceEvent(searchAppearanceEvent);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    public void testSaveSearchAppearanceEventErrorNull() {
        assertThrows(IllegalArgumentException.class, () -> analyticsEventService.saveSearchAppearanceEvent(null));

        verify(analyticsEventRepository, never()).save(analyticsEvent);
    }
}
