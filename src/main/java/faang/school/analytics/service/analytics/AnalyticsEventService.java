package faang.school.analytics.service.analytics;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;

    public AnalyticsEvent save(AnalyticsEvent event) {
        return analyticsEventRepository.save(event);
    }
    public Iterable<AnalyticsEvent> findAll() {
        return analyticsEventRepository.findAll();
    }
    public List<AnalyticsEvent> findByRecipientId(Long recipientId) {
        return analyticsEventRepository.findByRecipientId(recipientId);
    }
}
