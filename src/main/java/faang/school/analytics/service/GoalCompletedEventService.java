package faang.school.analytics.service;

import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.mapper.GoalCompletedEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoalCompletedEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final GoalCompletedEventMapper goalCompletedEventMapper;

    public void save(GoalCompletedEvent goalCompletedEvent){
        AnalyticsEvent analyticsEvent = goalCompletedEventMapper.toAnalyticsEvent(goalCompletedEvent);
        analyticsEventRepository.save(analyticsEvent);
    }
}
