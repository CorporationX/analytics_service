package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;

    public AnalyticsEvent save(AnalyticsEvent analyticsEvent) {
        AnalyticsEvent savedAnalyticsEvent = analyticsEventRepository.save(analyticsEvent);
        log.info("Analytics event with ID {} was saved", savedAnalyticsEvent.getId());
        return savedAnalyticsEvent;
    }

    public void deleteById (long id) {
        analyticsEventRepository.deleteById(id);
        log.info("Analytics event with ID {} was deleted", id);
    }

    public AnalyticsEvent getById(long id) {
        String exceptionMessage = String.format("Analytics event with id = %s not found", id);
        return analyticsEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(exceptionMessage));
    }
}