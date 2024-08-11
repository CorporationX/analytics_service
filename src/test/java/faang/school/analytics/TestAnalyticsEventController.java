package faang.school.analytics;

import faang.school.analytics.controller.AnalyticsEventController;
import faang.school.analytics.model.EventType;
import faang.school.analytics.services.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

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
        assertThrows(IllegalArgumentException.class, () -> analyticsEventController.getAnalyticsEvents(receiverId,"post_like",null,null,null));
    }

    @Test
    public void testGetAnalyticsEventNotCompletelyFilledRange() {
        long receiverId = 1L;
        EventType eventType = EventType.POST_LIKE;
        LocalDateTime from = LocalDateTime.of(2024,5,15,15,0);
        assertThrows(IllegalArgumentException.class, () -> analyticsEventController.getAnalyticsEvents(receiverId,"post_like",null,from,null));
    }
    @Test
    public void testGetAnalyticsEventBothIntervals() {
        long receiverId = 1L;
        EventType eventType = EventType.POST_LIKE;
        LocalDateTime from = LocalDateTime.of(2024,5,15,15,46);
        LocalDateTime to = LocalDateTime.of(2024,7,27,15,46);
        assertThrows(IllegalArgumentException.class, () -> analyticsEventController.getAnalyticsEvents(receiverId,"post_like","WEEK",from,to));
    }
}
