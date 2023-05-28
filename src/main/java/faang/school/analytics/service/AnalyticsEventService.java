package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.Interval;
import faang.school.analytics.dto.event.ProfileViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional
    public void saveEvent(ProfileViewEvent event) {
        AnalyticsEvent view = analyticsEventMapper.toEntity(event);
        analyticsEventRepository.save(view);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval,
                                                LocalDateTime from, LocalDateTime to) {
        Stream<AnalyticsEvent> views = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
        if (interval != null) {
            views = views.filter(view -> view.getReceivedAt().isAfter(interval.getFrom()));
        } else {
            views = views.filter(view -> view.getReceivedAt().isAfter(from) && view.getReceivedAt().isBefore(to));
        }
        return views.sorted((v1, v2) -> v2.getReceivedAt().compareTo(v1.getReceivedAt()))
                .map(analyticsEventMapper::toDto)
                .toList();
    }
}
