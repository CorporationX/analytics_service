package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    public void test_saveEventAnalytics_Successful(){
        AnalyticsEvent analyticsEvent = new AnalyticsEvent(1, 1, 5, EventType.PROFILE_VIEW, LocalDateTime.now());
        Assertions.assertDoesNotThrow(() -> analyticsEventService.saveEventAnalytics(analyticsEvent));
        verify(analyticsEventRepository).save(analyticsEvent);
    }
}
