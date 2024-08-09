package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    public void testSaveAnalyticsEvent() {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        analyticsEventService.save(analyticsEvent);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }
}
