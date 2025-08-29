package faang.school.analytics.service;

import faang.school.analytics.dto.MentorshipEventDto;
import faang.school.analytics.mapper.MentorshipEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {

    private final AnalyticsEventRepository repository;
    private final MentorshipEventMapper mapper;

    @Transactional
    public void saveEvent(MentorshipEventDto eventDto) {
        if (eventDto.getSenderId() <= 0 || eventDto.getReceiverId() <= 0) {
            log.warn("Invalid user IDs: actorId={}, receiverId={}", eventDto.getSenderId(), eventDto.getReceiverId());
            throw new IllegalArgumentException("User IDs must be greater than zero");
        }

        if (eventDto.getTimestamp() == null) {
            log.warn("Timestamp is null for event: {}", eventDto);
            throw new IllegalArgumentException("Timestamp cannot be null");
        }

        AnalyticsEvent event = mapper.toEntity(eventDto);
        repository.save(event);
        log.info("Сохранено событие: sender={}, receiver={}", event.getActorId(), event.getReceiverId());
    }

    @Transactional(readOnly = true)
    public List<MentorshipEventDto> getUserMentorshipAnalytics(long userId, LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null && from.isAfter(to)) {
            log.warn("Invalid date range: from={}, to={} for userId={}", from, to, userId);
            throw new IllegalArgumentException("Parameter 'from' must not be after 'to'");
        }

        Stream<AnalyticsEvent>eventsStream = repository.findByReceiverIdAndEventType(userId, EventType.PROJECT_INVITE);

        List<AnalyticsEvent> filteredEvents = eventsStream
                .filter(event -> isWithinRange(event.getReceivedAt(), from, to))
                .toList();

        return filteredEvents.stream()
                .map(mapper::toDto)
                .toList();
    }

    private boolean isWithinRange(LocalDateTime timestamp, LocalDateTime from, LocalDateTime to) {
        boolean afterFrom = from == null || !timestamp.isBefore(from);
        boolean beforeTo = to == null || !timestamp.isAfter(to);
        return afterFrom && beforeTo;
    }
}