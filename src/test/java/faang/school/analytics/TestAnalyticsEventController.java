package faang.school.analytics;

import faang.school.analytics.controller.analyticsEvent.AnalyticsEventController;
import faang.school.analytics.filters.timeManagment.Interval;
import faang.school.analytics.model.EventType;
import faang.school.analytics.services.AnalyticsEventService;
import faang.school.analytics.services.utils.AnalyticsUtilService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class TestAnalyticsEventController {
    @Mock
    private AnalyticsEventService analyticsEventService;
    @InjectMocks
    private AnalyticsEventController analyticsEventController;

    @Test
    public void testGetAnalyticsEvent() {

    }

    @Test
    public void testGetAnalyticsEventNoIntervals() {
        long receiverId = 1L;
        EventType eventType = EventType.POST_LIKE;
        assertThrows(IllegalArgumentException.class, () -> analyticsEventController.getAnalyticsEvent(receiverId,"post_like","","",""));
    }

    @Test
    public void testGetAnalyticsEventNotCompletelyFilledRange() {
        long receiverId = 1L;
        EventType eventType = EventType.POST_LIKE;
        assertThrows(IllegalArgumentException.class, () -> analyticsEventController.getAnalyticsEvent(receiverId,"post_like","","15-05-2024 15:46:00",""));
    }
    @Test
    public void testGetAnalyticsEventBothIntervals() {
        long receiverId = 1L;
        EventType eventType = EventType.POST_LIKE;
        assertThrows(IllegalArgumentException.class, () -> analyticsEventController.getAnalyticsEvent(receiverId,"post_like","TWO_WEEKS_BEFORE_TIL_NOW","15-05-2024 15:46:00","27-07-2024 15:46:00"));
    }

    @Test
    public void testWrongDateFormat(){
        long receiverId = 1L;
        EventType eventType = EventType.POST_LIKE;
        assertThrows(DateTimeParseException.class, () -> analyticsEventController.getAnalyticsEvent(receiverId,"post_like","","15-05-24 15:46:00","27-07-2024 15:46:00"));
    }
}
