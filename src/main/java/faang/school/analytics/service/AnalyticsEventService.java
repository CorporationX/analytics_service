package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository repository;
    private final AnalyticsEventMapper mapper;

    @Transactional
    public AnalyticsEventDto saveEvent(AnalyticsEvent event) {
        return mapper.toDto(repository.save(event));
    }

    @Transactional
    public void save(AnalyticsEvent event) {
        repository.save(event);
    }

    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval,
                                                LocalDateTime from, LocalDateTime to) {
        return repository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(event -> {
                    if (interval == null) {
                        return event.getReceivedAt().isAfter(from) && event.getReceivedAt().isBefore(to);
                    }
                    return event.getReceivedAt().isAfter(interval.getDate());
                })
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(mapper::toDto)
                .toList();
    }
}
