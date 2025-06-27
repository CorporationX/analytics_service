package faang.school.analytics.service.event;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.event.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;

    @Override
    public AnalyticsEvent saveFollowerEvent(FollowerEvent followerEvent) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticEvent(followerEvent);
        return analyticsEventRepository.save(analyticsEvent);
    }
}
