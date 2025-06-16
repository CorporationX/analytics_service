package faang.school.analytics.service;

import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.mapper.goalcompleted.UserServiceEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final UserServiceEventMapper userServiceEventMapper;

    @Override
    public void saveGoalCompleteEvent(GoalCompletedEvent event) {
        AnalyticsEvent analyticsEvent = userServiceEventMapper.goalCompleteToAnalytics(event);
        analyticsEventRepository.save(analyticsEvent);
    }
}
