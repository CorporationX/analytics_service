package faang.school.analytics;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GoalCompletedEventDtoServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    public void goalCompletedEventServiceSave() {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent(1, 1, 1, EventType.GOAL_COMPLETED, LocalDateTime.now());
        Assertions.assertDoesNotThrow(() -> analyticsEventService.save(analyticsEvent));
        verify(analyticsEventRepository).save(analyticsEvent);
    }
}
