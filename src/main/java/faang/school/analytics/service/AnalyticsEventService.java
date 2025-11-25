package faang.school.analytics.service;

import faang.school.analytics.exeption.AnalyticsEventSaveException;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;

    @Transactional
    public AnalyticsEvent save(AnalyticsEvent event) {

        if (event == null) {
            throw new IllegalArgumentException("Analytics event cannot be null");
        }

        try {
            AnalyticsEvent saved = analyticsEventRepository.save(event);
            logEventSaved(saved);
            return saved;
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation when saving analytics event: receiverId={}, actorId={}, eventType={}",
                    event.getReceiverId(), event.getActorId(), event.getEventType(), e);
            throw new AnalyticsEventSaveException("Failed to save analytics event due to data integrity violation", e);
        } catch (Exception e) {
            log.error("Unexpected error saving analytics event: receiverId={}, actorId={}, eventType={}",
                    event.getReceiverId(), event.getActorId(), event.getEventType(), e);
            throw new AnalyticsEventSaveException("Failed to save analytics event", e);
        }
    }

    private void logEventSaved(AnalyticsEvent saved) {
        log.info("Saved analytics event: id={}, receiverId={}, actorId={}, eventType={}, receivedAt={}",
                saved.getId(),
                saved.getReceiverId(),
                saved.getActorId(),
                saved.getEventType(),
                saved.getReceivedAt());
    }
}
