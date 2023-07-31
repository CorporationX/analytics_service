package faang.school.analytics.service.analytics;

import faang.school.analytics.dto.AnalyticDto;
import faang.school.analytics.mapper.AnalyticsMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final AnalyticsEventRepository analyticsEventRepository;

    private final AnalyticsMapper analyticsMapper;

    public AnalyticDto create(AnalyticDto analyticDto) {
        AnalyticsEvent createdEvent = analyticsEventRepository.save(analyticsMapper.toEntity(analyticDto));
        return analyticsMapper.toDto(createdEvent);
    }
}
