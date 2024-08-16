package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {
    @InjectMocks
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    public void setUp(){
        analyticsEvent = new AnalyticsEvent();
    }

    @Test
    @DisplayName("testing saveAnalyticsEventMethod")
    void testSaveAnalyticsEvent() {
        analyticsEventService.saveAnalyticsEvent(analyticsEvent);
        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }
}