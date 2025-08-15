package faang.school.analytics.service;

import faang.school.analytics.dto.MentorshipEventDto;
import faang.school.analytics.repository.MentorshipEventRepository;
import faang.school.analytics.mapper.MentorshipEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MentorshipEventService {

    private final MentorshipEventRepository repository;
    private final MentorshipEventMapper mapper;

    public void processMentorshipEvent(MentorshipEventDto eventDto) {
        saveEvent(eventDto);
    }

    public List<MentorshipEventDto> getUserMentorshipAnalytics(long userId, LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null && from.isAfter(to)) {
            log.warn("Invalid date range: from={}, to={} for userId={}", from, to, userId);
            throw new IllegalArgumentException("Parameter 'from' must not be after 'to'");
        }

        return List.of();
    }

    public void saveEvent(MentorshipEventDto event) {
        if (event.getSenderId() <= 0 || event.getReceiverId() <= 0) {
            log.warn("Invalid user IDs: senderId={}, receiverId={}", event.getSenderId(), event.getReceiverId());
            throw new IllegalArgumentException("User IDs must be greater than zero");
        }

        if (event.getTimestamp() == null) {
            log.warn("Timestamp is null for event: {}", event);
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
        repository.save(event);
    }
}