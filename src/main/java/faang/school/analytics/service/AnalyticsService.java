package faang.school.analytics.service;

import faang.school.analytics.dto.MentorshipEventDto;
import faang.school.analytics.exception.Exceptional;
import faang.school.analytics.repository.MentorshipEventRepository;
import faang.school.analytics.mapper.MentorshipEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final MentorshipEventRepository repository;
    private final MentorshipEventMapper mapper;

    public void processMentorshipEvent(MentorshipEventDto eventDto) {
        saveEvent(eventDto);
    }

    public List<MentorshipEventDto> getAnalytics(long userId, LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null && from.isAfter(to)) {
            Exceptional.throwInvalidDateRange();
        }
        return null;
    }

    public void saveEvent(MentorshipEventDto event) {
        if (event.getSenderId() <= 0 || event.getReceiverId() <= 0) {
            Exceptional.throwInvalidIds();
        }
        if (event.getTimestamp() == null) {
            Exceptional.throwNullTimestamp();
        }
        repository.save(event);
    }
}