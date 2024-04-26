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

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository repository;
    @InjectMocks
    private AnalyticsEventService service;

    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    public void setUp() {
        analyticsEvent = AnalyticsEvent.builder()
                .receiverId(1L)
                .actorId(2L)
                .receivedAt(LocalDateTime.now())
                .build();

    }

    @Test
    @DisplayName("Checking that the object is saved")
    public void testSave() {
        when(repository.save(analyticsEvent)).thenReturn(analyticsEvent);

        service.save(analyticsEvent);

        verify(repository, times(1)).save(analyticsEvent);
    }
}
