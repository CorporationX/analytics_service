package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.filter.AnalyticsEventFilter;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
@Builder
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final List<AnalyticsEventFilter> analyticsEventFilters;

    @Transactional
    public void saveEvent(AnalyticsEventDto analyticsEventDto) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(analyticsEventDto);
        analyticsEventRepository.save(analyticsEvent);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(AnalyticsEventFilterDto filterDto) {
        Stream<AnalyticsEvent> analyticsEvents = analyticsEventRepository
                .findByReceiverIdAndEventTypeOrderByReceiverIdDesc(filterDto.getReceiverId(), filterDto.getEventType());

        for (AnalyticsEventFilter analyticsEventFilter : analyticsEventFilters) {
            analyticsEvents = analyticsEventFilter.filter(analyticsEvents, filterDto);
        }

        return analyticsEvents.map(analyticsEventMapper::toDto).toList();
    }
}
