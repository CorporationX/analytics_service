package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.MentorshipRequestedEvent;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.dto.follower.FollowerEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;

    public void saveEvent(EventType eventType, AnalyticsEvent analyticsEvent) {
        analyticsEvent.setEventType(eventType);
        analyticsEventRepository.save(analyticsEvent);
        log.info(analyticsEvent + " is saved to DB");
    }

    public void saveRecommendationEvent(RecommendationEvent recommendationEvent) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.recomendationEventToAnalyticsEvent(recommendationEvent);
        saveEvent(EventType.RECOMMENDATION_RECEIVED, analyticsEvent);
    }

    public void saveMentorshipRequestedEvent(MentorshipRequestedEvent mentorshipRequestedEvent) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.mentorshipRequestedEventToAnalyticsEvent(mentorshipRequestedEvent);
        saveEvent(EventType.MENTORSHIP_REQUESTED, analyticsEvent);
    }

    public void saveFollowerEvent(FollowerEventDto followerEventDto) {
        AnalyticsEvent followerEvent = analyticsEventMapper.toEntity(followerEventDto);
        followerEvent.setEventType(EventType.FOLLOWER);
        log.info("Старт saveAnalyticsEvent: {}", followerEventDto);
        analyticsEventRepository.save(followerEvent);
        log.info("Сохранен AnalyticsEvent - {} ", followerEvent);
    }

    public void saveEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
        log.info(event + "is saved to DB");
    }

    public List<AnalyticsEventDto> getAnalytics(long receiverId,
                                                EventType eventType,
                                                Interval interval,
                                                LocalDateTime from,
                                                LocalDateTime to) {
        List<AnalyticsEvent> analyticsEvents = analyticsEventRepository
                .findByReceiverIdAndEventType(receiverId, eventType)
                .toList();
        if (interval != null) {
            analyticsEvents = filterWithInterval(analyticsEvents, interval);
        }
        if (interval == null) {
            analyticsEvents = filterWithFromTo(analyticsEvents, from, to);
        }
        return analyticsEvents.stream()
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(analyticsEventMapper::toAnalyticsDto)
                .toList();
    }

    private List<AnalyticsEvent> filterWithInterval(List<AnalyticsEvent> analyticsEventStream,
                                                    Interval interval) {
        return analyticsEventStream.stream()
                .filter(receiverAt ->
                        receiverAt.getReceivedAt().isAfter(interval.getTo()) &&
                                receiverAt.getReceivedAt().isBefore(interval.getFrom()))
                .toList();
    }

    private List<AnalyticsEvent> filterWithFromTo(List<AnalyticsEvent> analyticsEventStream,
                                                  LocalDateTime from,
                                                  LocalDateTime to) {
        return analyticsEventStream.stream()
                .filter(receiverAt ->
                        receiverAt.getReceivedAt().isAfter(from) &&
                                receiverAt.getReceivedAt().isBefore(to))
                .toList();
    }
}
