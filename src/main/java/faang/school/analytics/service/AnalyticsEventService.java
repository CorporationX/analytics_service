package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.filter.AnalyticsEventFilter;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final List<AnalyticsEventFilter> analyticsEventFilters;

    @Transactional
    public AnalyticsEventDto saveEvent(AnalyticsEvent event) {

        return analyticsEventMapper.analyticsEventToAnalyticsEventDto(analyticsEventRepository.save(event));
    }

    public List<AnalyticsEventDto> getAnalytics(AnalyticsEventFilterDto filterDto) {
        Stream<AnalyticsEvent> analyticsEvents = findByReceiverIdAndEventType(
                filterDto.getReceiverId(), filterDto.getEventType());

        return analyticsEventMapper.analyticsEventListToAnalyticsEventDtoList(
                analyticsEventFilters.stream()
                        .filter(analyticsEventFilter -> analyticsEventFilter.isApplicable(filterDto))
                        .reduce(analyticsEvents, (filtered, filter) ->
                                filter.apply(filtered, filterDto), (a, b) -> b).toList());
    }

    private Stream<AnalyticsEvent> findByReceiverIdAndEventType(long receiverId, EventType eventType) {
        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
    }
}
