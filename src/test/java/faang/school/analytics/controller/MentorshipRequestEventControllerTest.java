package faang.school.analytics.controller;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MentorshipRequestEventControllerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;
    @InjectMocks
    private MentorshipRequestEventController mentorshipRequestEventController;

    @Test
    void successGetAnalytics(){
        AnalyticsEvent event = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(2L)
                .build();
        String type = "MENTORSHIP_REQUEST";
        long receiverId = event.getReceiverId();
        EventType eventType = EventType.valueOf(type.toUpperCase(Locale.ROOT));

        mentorshipRequestEventController.getAnalytics(receiverId, type);
        verify(analyticsEventService, times(1)).getAnalytics(receiverId, eventType);
    }
}
