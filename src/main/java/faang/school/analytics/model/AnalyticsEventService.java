package faang.school.analytics.model;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.dto.SearchAppearanceEvent;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper mapper;

    // TODO create tests
    public void createAnalyticsEvent(SearchAppearanceEvent searchAppearanceEvent) {
        AnalyticsEvent analyticsEvent = mapper.toEntity(searchAppearanceEvent);
        analyticsEventRepository.save(analyticsEvent);
    }
}
