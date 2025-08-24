package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository repository;

    @Transactional
    public AnalyticsEvent saveEvent(@Valid AnalyticsEvent event) {
        return repository.save(event);
    }
}
