package faang.school.analytics.service;

import faang.school.analytics.dto.MentorshipRequestedEvent;
import faang.school.analytics.mapper.MentorshipRequestedMapper;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorshipRequestedEventService {
    private final AnalyticsEventService analyticsEventService;
    private final MentorshipRequestedMapper mentorshipRequestedMapper;

    public void save(MentorshipRequestedEvent request) {
        AnalyticsEvent analyticsEvent = mentorshipRequestedMapper.toAnalyticsEvent(request);
        analyticsEventService.save(analyticsEvent);
    }
}
