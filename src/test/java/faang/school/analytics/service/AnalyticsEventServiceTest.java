package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @Mock
    AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    AnalyticsEventService analyticsEventService;

    @Test
    public void saveEventTest() {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        analyticsEventService.saveEvent(analyticsEvent);

        verify(analyticsEventRepository).save(analyticsEvent);
    }
}