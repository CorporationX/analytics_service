package faang.school.analytics.listener;

import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.dto.MentorshipRequestedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class MentorshipRequestedEventListenerTest {

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;
    @InjectMocks
    private MentorshipRequestedEventListener mentorshipRequestedEventListener;


    @Test
    void shouldSave() {
        MentorshipRequestedEvent event = new MentorshipRequestedEvent();
        mentorshipRequestedEventListener.processEvent(event);
        verify(analyticsEventMapper, times(1)).toAnalyticsEvent(event);
        verify(analyticsEventService, times(1)).
                saveEvent(AnalyticsEvent.builder().eventType(EventType.MENTORSHIP_REQUESTED).build());
    }
}