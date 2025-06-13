package faang.school.analytics.service.analytics;

import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.mapper.like.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void addLikeEvent(LikeEvent event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.likeEventToAnalytics(event);
        analyticsEventRepository.save(analyticsEvent);
    }
}
