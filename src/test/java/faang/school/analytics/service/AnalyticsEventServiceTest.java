package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    void testSave_AnalyticsEventSaved() {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        analyticsEventService.save(analyticsEvent);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    void testSave_TransactionRollbackOnError() {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        doThrow(RuntimeException.class).when(analyticsEventRepository).save(any(AnalyticsEvent.class));

        assertThrows(RuntimeException.class, () -> analyticsEventService.save(analyticsEvent));
    }
}