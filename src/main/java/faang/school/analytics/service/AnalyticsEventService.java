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

/**
 * @author Evgenii Malkov
 */
@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(long receiverId,
                                                EventType eventType,
                                                Interval interval,
                                                LocalDateTime from,
                                                LocalDateTime to) {

        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter((event) -> interval != null
                        ? interval.includes(event.getReceivedAt())
                        : !event.getReceivedAt().isBefore(from) && !event.getReceivedAt().isAfter(to))
                .map(analyticsEventMapper::toDto)
                .sorted(Comparator.comparing(AnalyticsEventDto::getReceivedAt).reversed())
                .toList();
    }

    @Transactional
    public void saveEvent(AnalyticsEventDto analyticsEventDto) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(analyticsEventDto);
        analyticsEvent.setReceivedAt(LocalDateTime.now());
        this.analyticsEventRepository.save(analyticsEvent);
    }

}
