package faang.school.analytics.service;

import faang.school.analytics.dto.RecomendationEventDto;
import faang.school.analytics.mapper.RecomendationEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecomendationAnalytics<T> implements AnalyticService<RecomendationEventDto> {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final RecomendationEventMapper recomendationEventMapper;

    @Override
    public void save(RecomendationEventDto eventDto) {

        AnalyticsEvent event = recomendationEventMapper.toEntity(eventDto);
        event.setEventType(EventType.RECOMMENDATION_RECEIVED);
        analyticsEventRepository.save(event);

    }

    @Override
    public boolean supportsEventType(RecomendationEventDto eventType) {
        return eventType.getClass() == RecomendationEventDto.class;
    }
}
