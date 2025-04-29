package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.event.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.dto.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public void saveEvent(AnalyticsEventDto analyticsEventDto) {
        analyticsEventRepository.save(analyticsEventMapper.toEntity(analyticsEventDto));
    }

    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval,
                                                LocalDateTime from, LocalDateTime to) {
        if (interval != null) {
            to = LocalDateTime.now();
            from = switch (interval) {
                case LAST_HOUR -> to.minusHours(1);
                case LAST_DAY -> to.minusDays(1);
                case LAST_WEEK -> to.minusWeeks(1);
                case LAST_MONTH -> to.minusMonths(1);
            };
        }
        LocalDateTime finalFrom = from;
        LocalDateTime finalTo = to;

        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(event -> event.getReceivedAt().isAfter(finalFrom) && event.getReceivedAt().isBefore(finalTo))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(analyticsEventMapper::toDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public void handleLikeEvent(LikeEvent event){
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .receiverId(event.getUserId())
                .actorId(event.getAuthorId())
                .eventType(EventType.POST_LIKE)
                .receivedAt(event.getLikedAt()).
                build();
        analyticsEventRepository.save(analyticsEvent);
    }
}