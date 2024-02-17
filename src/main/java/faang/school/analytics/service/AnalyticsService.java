package faang.school.analytics.service;

import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService{

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper mapper;

    public void saveEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
    }
}
