package faang.school.analytics.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CreateAnalyticsEventDto;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {
    private final AnalyticsEventRepository repository;
    private final AnalyticsEventMapper mapper;

    @Transactional
    @Override
    public AnalyticsEventDto saveEvent(CreateAnalyticsEventDto dto) {
        AnalyticsEvent event = mapper.toModel(dto);
        repository.save(event);
        return mapper.toDto(event);
    }

    @Transactional
    @Override
    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval,
            LocalDateTime from, LocalDateTime to) {
        if (eventType == null) {
            log.warn("Attempting to get events without eventType");
            throw new DataValidationException("You must specify the eventType");
        }

        LocalDateTime actualTo = Optional.ofNullable(to).orElse(LocalDateTime.now());
        LocalDateTime actualFrom = Optional.ofNullable(from)
            .orElseGet(() -> interval != null ? interval.subtractFrom(actualTo) : null);

        if (actualFrom == null) {
            log.warn("Attempting to get events without from time");
            throw new DataValidationException("You must specify the `from` field or specify `Interval`");
        }

        List<AnalyticsEvent> analyticsEvents = repository.findEvents(receiverId, eventType, actualFrom, actualTo);
        return analyticsEvents.stream()
                .map(mapper::toDto)
                .toList();
    }
}

