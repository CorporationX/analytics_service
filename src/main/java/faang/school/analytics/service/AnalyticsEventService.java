package faang.school.analytics.service;


import faang.school.analytics.mapper.AnalyticMapper;
import faang.school.analytics.redis.MentorshipRequestEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    public final AnalyticsEventRepository analyticsEventRepository;
    public final AnalyticMapper mapper;

    public void saveAnalyticEvent(MentorshipRequestEvent mentorshipRequestEvent) {
        analyticsEventRepository.save(mapper.toAnalytic(mentorshipRequestEvent));
    }
}
