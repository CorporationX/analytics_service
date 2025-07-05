package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository repository;

    @InjectMocks
    private AnalyticsEventService service;

    @Test
    void save_delegatesToRepository() {
        AnalyticsEvent event = new AnalyticsEvent();
        service.save(event);
        verify(repository).save(event);
        verifyNoMoreInteractions(repository);
    }
}