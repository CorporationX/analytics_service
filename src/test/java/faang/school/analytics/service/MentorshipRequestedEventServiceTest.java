package faang.school.analytics.service;

import faang.school.analytics.dto.MentorshipRequestedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;


@ExtendWith(MockitoExtension.class)
class MentorshipRequestedEventServiceTest {
    @Spy
    private AnalyticsEventMapper mentorshipRequestedMapper = new AnalyticsEventMapperImpl();
    @Mock
    private AnalyticsEventService analyticsEventService;
    @InjectMocks
    private MentorshipRequestedEventService mentorshipRequestedEventService;

    @Test
    void shouldSave() {
        MentorshipRequestedEvent event = new MentorshipRequestedEvent(1L, 2L, LocalDateTime.now());
        mentorshipRequestedEventService.save(event);
        Mockito.verify(analyticsEventService, Mockito.times(1)).save(mentorshipRequestedMapper.toAnalyticsEvent(event));
    }
}