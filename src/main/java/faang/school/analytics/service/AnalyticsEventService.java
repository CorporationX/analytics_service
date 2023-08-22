package faang.school.analytics.service;

import faang.school.analytics.dto.SearchAppearanceEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional
    public void save(SearchAppearanceEventDto event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(event);
        analyticsEventRepository.save(analyticsEvent);
        log.debug("Analytics event saved: {}", analyticsEvent);
    }
}
