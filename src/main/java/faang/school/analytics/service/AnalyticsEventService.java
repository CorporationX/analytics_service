package faang.school.analytics.service;

import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.dto.ProfileViewEvent;
import faang.school.analytics.mapper.PostViewEventMapper;
import faang.school.analytics.mapper.ProfileViewEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository repository;

    @Transactional
    public void processProfileViewEvent(ProfileViewEvent event) {
        AnalyticsEvent analyticsEvent = ProfileViewEventMapper.toAnalyticsEvent(event);
        AnalyticsEvent savedEvent = repository.save(analyticsEvent);

        log.info("Successfully saved AnalyticsEvent with id={}, eventType={}, receiverId={}, actorId={}",
                savedEvent.getId(), savedEvent.getEventType(), savedEvent.getReceiverId(), savedEvent.getActorId());
    }

    @Transactional
    public void processPostViewEvent(PostViewEvent event) {
        AnalyticsEvent analyticsEvent = PostViewEventMapper.toAnalyticsEvent(event);
        AnalyticsEvent savedEvent = repository.save(analyticsEvent);

        log.info("Successfully saved PostView AnalyticsEvent with id={}, eventType={}, postId={}, viewerId={}",
                savedEvent.getId(), savedEvent.getEventType(), event.postId(), event.viewerId());
    }
}

