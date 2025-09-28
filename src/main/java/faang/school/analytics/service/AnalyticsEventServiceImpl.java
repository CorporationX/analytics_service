package faang.school.analytics.service;

import faang.school.analytics.messagelistener.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.mentorshipevent.MentorshipRequestedEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    MentorshipRequestedEvent mentorshipRequestedEvent;

    @Override
    @Transactional
    public AnalyticsEvent save() {

        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(mentorshipRequestedEvent);
        validation(analyticsEvent);


        log.info("Mentorship (id={}) has been saved", analyticsEvent.getId());
        analyticsEventRepository.save(analyticsEvent.getReceiverId(), analyticsEvent.getActorId(),
                analyticsEvent.getReceivedAt());
        return analyticsEvent;
    }

    private void validation(AnalyticsEvent event) {
        String message = String.format(
                "Mentorship (id=%d) already exists", event.getId()
        );
        if (analyticsEventRepository.existsById(event.getId())) {
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

}
