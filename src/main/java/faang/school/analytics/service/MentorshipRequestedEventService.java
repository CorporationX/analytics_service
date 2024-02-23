package faang.school.analytics.service;

import faang.school.analytics.dto.MentorshipRequestedEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MentorshipRequestedEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public void saveEvent(MentorshipRequestedEventDto eventDto) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(eventDto);
        analyticsEvent.setEventType(EventType.MENTORSHIP_REQUEST);
        analyticsEventRepository.save(analyticsEvent);
    }

    @Transactional
    public Stream<AnalyticsEvent> getAnalytics(long receiverId, EventType type) {
        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, type);
    }
}
