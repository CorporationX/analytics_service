package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    private AnalyticsEvent event;

    @BeforeEach
    public void setUp() {
        event = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(1L)
                .actorId(1L)
                .receivedAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void testSave() {
        analyticsEventService.save(event);

        verify(analyticsEventRepository, times(1)).save(event);
    }
}