package faang.school.analytics.service.impl;

import faang.school.analytics.dto.SearchAppearanceEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper mapper;

    @Override
    public void saveSearchAppearanceEvent(SearchAppearanceEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("event is null");
        }
        analyticsEventRepository.save(mapper.mapSearchAppearanceToAnalyticEvent(event));
    }
}
