package faang.school.analytics.service;

import faang.school.analytics.exception.DuplicatedEventException;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@RequiredArgsConstructor
@Service
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;

    @Transactional
    public void addEvent(@NotNull AnalyticsEvent analyticsEvent) {
        checkEvent(analyticsEvent);
        AnalyticsEvent savedEvent = analyticsEventRepository.save(analyticsEvent);
        log.info("Added event: {}", savedEvent);
    }

    private void checkEvent(AnalyticsEvent analyticsEvent) {
        AnalyticsEvent eventFromDb = analyticsEventRepository.findByActorIdAndReceiverIdAndEventType(
                analyticsEvent.getActorId(), analyticsEvent.getReceiverId(), analyticsEvent.getEventType());

        if (eventFromDb != null) {
            throw new DuplicatedEventException("Duplicated event actor %d and receiver %d"
                    .formatted(analyticsEvent.getActorId(), analyticsEvent.getReceiverId()));
        }
    }
}
