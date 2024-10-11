package faang.school.analytics.service;

import faang.school.analytics.event.ProjectViewEvent;
import faang.school.analytics.mapper.AnalyticMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticServiceImpl implements AnalyticService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticMapper analyticMapper;

    @Override
    public void saveAnalyticEvent(ProjectViewEvent event) {
        AnalyticsEvent analyticsEvent = analyticMapper.toEntity(event);
        analyticsEvent.setEventType(EventType.PROJECT_VIEW);
        analyticsEventRepository.save(analyticsEvent);
    }
}
