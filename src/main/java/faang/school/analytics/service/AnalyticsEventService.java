package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;

    public AnalyticsEvent save(AnalyticsEvent analyticsEvent) {
        return analyticsEventRepository.save(analyticsEvent);
    }

    public void deleteById (long id) {
        analyticsEventRepository.deleteById(id);
    }

    public AnalyticsEvent getById(long id) {
        String exceptionMessage = String.format("Analytics event with id = %s not found", id);
        return analyticsEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(exceptionMessage));
    }
}