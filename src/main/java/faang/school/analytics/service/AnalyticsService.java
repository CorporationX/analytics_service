package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsValidationService validationService;

    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType,
                                                Optional<Interval> interval,
                                                LocalDateTime from, LocalDateTime to) {
        validationService.validateAnalyticsRequest(interval, from, to);

        Stream<AnalyticsEvent> events = analyticsEventRepository
                .findByReceiverIdAndEventType(receiverId, eventType);

        return events
                .map(analyticsEventMapper::toDto)
                .collect(Collectors.toList());
    }

    public AnalyticsEventDto saveEvent(AnalyticsEventDto analyticsEventDto) {
        AnalyticsEvent entity = analyticsEventMapper.toEntity(analyticsEventDto);
        AnalyticsEvent savedEntity = analyticsEventRepository.save(entity);
        return analyticsEventMapper.toDto(savedEntity);
    }
}
