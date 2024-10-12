package faang.school.analytics.service;


import faang.school.analytics.mapper.AnalyticMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.redis.MentorshipRequestEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    public final AnalyticsEventRepository analyticsEventRepository;
    public final AnalyticMapper mapper;

    @Override
    public void saveAnalyticEvent(MentorshipRequestEvent mentorshipRequestEvent) {
        AnalyticsEvent analytic = mapper.toAnalytic(mentorshipRequestEvent);
        analytic.setEventType(EventType.MENTORSHIP_REQUEST_VIEW);
        analyticsEventRepository.save(analytic);
    }
}
