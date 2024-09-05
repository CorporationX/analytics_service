package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.filter.AnalyticsFilter;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AnalyticsEventService {

    private final AnalyticsEventRepository repository;
    private final AnalyticsEventMapper mapper;
    private final List<AnalyticsFilter> analyticsFilters;

    public void saveEvent(AnalyticsEvent event) {
        if (event == null) {
            throw new RuntimeException("Event can't be empty");
        }
        repository.save(event);
    }

    public List<AnalyticsEventDto> getAnalytics(AnalyticsFilterDto filters) {
        AnalyticsFilter actualFilter = analyticsFilters
                .stream()
                .filter(filter -> filter.isApplicable(filters))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No applicable filter found"));

        return repository.findByReceiverIdAndEventType(filters.getReceiverId(), filters.getEventType())
                .filter(event -> actualFilter.action(event, filters))
                .map(mapper::toDto)
                .toList();
    }
}
