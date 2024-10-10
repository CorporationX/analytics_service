package faang.school.analytics.service.impl.analyticsevent;

import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceImplTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    @Test
    @DisplayName("Send Event Test")
    void saveEvent_isOk() {
        var analyticsEvent = new AnalyticsEvent();
        analyticsEventService.saveEvent(analyticsEvent);
        verify(analyticsEventRepository).save(any(AnalyticsEvent.class));
        verifyNoMoreInteractions(analyticsEventRepository);
    }
}
