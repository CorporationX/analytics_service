package faang.school.analytics.service;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.SearchAppearanceEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventMapper mapper;
    private final AnalyticsEventRepository analyticsEventRepository;

    @Override
    public void saveSearchAppearanceEvent(SearchAppearanceEvent event) {
        AnalyticsEvent analyticsEvent = mapper.toEntity(event, EventType.PROFILE_APPEARED_IN_SEARCH.name());
        analyticsEventRepository.save(analyticsEvent);
    }
}
