package faang.school.analytics.service.analytics;

import faang.school.analytics.dto.AnalyticEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    @Setter
    @Value("${analytics.latest_events_period_of_time}")
    private String latestPeriodOfTime;

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public AnalyticsEvent create(AnalyticsEvent analyticsEvent) {
        return analyticsEventRepository.save(analyticsEvent);
    }

    public List<AnalyticEventDto> getLatestEvents() {
        List<AnalyticsEvent> analyticsEvents = analyticsEventRepository.getLatestEvents(latestPeriodOfTime);
        return analyticsEvents.stream().map(analyticsEventMapper::toDto).toList();
    }
}
