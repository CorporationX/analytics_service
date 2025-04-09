package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.filter.AnalyticsEventFilter;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final List<AnalyticsEventFilter> filters;

    public void saveEvent(AnalyticsEventDto eventDto) {
        analyticsRepository.save(analyticsEventMapper.toEntity(eventDto));
        log.info("Saved new event: {}", eventDto);
    }

    public List<AnalyticsEventDto> getAnalytics(AnalyticsEventFilterDto filter) {
        Specification<AnalyticsEvent> specifications = filters.stream()
                .filter(eventFilter -> eventFilter.isApplicable(filter))
                .map(eventFilter -> eventFilter.apply(filter))
                .reduce(Specification::and)
                .orElse(null);

        List<AnalyticsEvent> analytics = specifications != null
                ? analyticsRepository.findAll(specifications)
                : analyticsRepository.findByReceiverIdAndEventType(filter.receiverId(), filter.eventType()).toList();

        return analyticsEventMapper.toDtoList(analytics.stream()
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .toList());
    }
}
