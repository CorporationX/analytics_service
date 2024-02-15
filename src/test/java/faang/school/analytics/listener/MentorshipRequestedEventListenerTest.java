package faang.school.analytics.listener;

import faang.school.analytics.dto.MentorshipRequestedEvent;
import faang.school.analytics.service.MentorshipRequestedEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;


@ExtendWith(MockitoExtension.class)
class MentorshipRequestedEventListenerTest {
    @Mock
    private MentorshipRequestedEventService mentorshipRequestedEventService;
    @InjectMocks
    private MentorshipRequestedEventListener mentorshipRequestedEventListener;

    @Test
    void shouldSave() {
        MentorshipRequestedEvent event = new MentorshipRequestedEvent(1L, 2L, LocalDateTime.now());
        mentorshipRequestedEventListener.processEvent(event);
        Mockito.verify(mentorshipRequestedEventService, Mockito.times(1)).save(event);
    }
}