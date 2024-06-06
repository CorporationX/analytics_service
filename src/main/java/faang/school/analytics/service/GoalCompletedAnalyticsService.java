package faang.school.analytics.service;

import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoalCompletedAnalyticsService implements AnalyticsService<GoalCompletedEvent> {

    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;

    @Override
    @Transactional
    public void save(GoalCompletedEvent event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
        analyticsEvent.setEventType(EventType.GOAL_COMPLETED);

        analyticsEventRepository.save(analyticsEvent);
    }
}
