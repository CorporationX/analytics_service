package faang.school.analytics.service;

import faang.school.analytics.Validator.AnalyticsEventValidator;
import faang.school.analytics.dto.event.AnalyticsEventResponseDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.specification.AnalyticsEventSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventValidator analyticsEventValidator;

    public void saveEvent(AnalyticsEvent event) {
        AnalyticsEvent savedEvent = analyticsEventRepository.save(event);
        log.info("Analytics event {} saved successfully.", savedEvent.getId());
    }

    public List<AnalyticsEventResponseDto> getAnalytics(
            Long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to) {

        Sort sort = Sort.by(Sort.Direction.DESC, "receivedAt");
        Specification<AnalyticsEvent> specification = Specification.where(
                        AnalyticsEventSpecification.hasReceiverId(receiverId))
                .and(AnalyticsEventSpecification.hasEventType(eventType));

        if (interval != null) {
            LocalDateTime intervalEnd = LocalDateTime.now(ZoneId.of("UTC"));
            LocalDateTime intervalStart = interval.calculateIntervalStart();
            specification = specification.and(AnalyticsEventSpecification.withinPeriod(intervalStart, intervalEnd));
        } else {
            analyticsEventValidator.validateDateOrder(from, to);
            specification = specification.and(AnalyticsEventSpecification.withinPeriod(from, to));
        }

        List<AnalyticsEventResponseDto> result = analyticsEventRepository.findAll(specification, sort).stream()
                .map(analyticsEventMapper::entityToResponseDto)
                .toList();
        log.info("Analytics retrieved successfully for receiverId {}. {} events found.", receiverId, result.size());

        return result;
    }
}
