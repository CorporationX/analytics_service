package faang.school.analytics.service.impl.analyticsevent;

import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;

    @Override
    @Transactional
    public void saveEvent(AnalyticsEvent event) {
        log.info("Saving follower event: {}", event);
        analyticsEventRepository.save(event);
        log.info("Saved follower event: {}", event);
    }
}
