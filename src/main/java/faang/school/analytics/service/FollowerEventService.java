package faang.school.analytics.service;

import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static faang.school.analytics.model.EventType.FOLLOWER;

@Component
@RequiredArgsConstructor
public class FollowerEventService {
    private final AnalyticsEventRepository analyticsEventRepository;

    public void save(FollowerEvent followerEvent) {
        AnalyticsEvent model = AnalyticsEvent.builder()
                .receiverId(followerEvent.getFolloweeId())
                .actorId(FOLLOWER.ordinal())
                .eventType(FOLLOWER)
                .receivedAt(followerEvent.getTimestamp())
                .build();
        analyticsEventRepository.save(model);
    }
}
