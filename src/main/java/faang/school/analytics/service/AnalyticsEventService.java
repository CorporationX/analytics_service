package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {
    private final AnalyticsEventRepository repo;

    public void save(AnalyticsEvent e) {
        repo.save(e);
        log.info(e.toString());
    }

    public Iterable<AnalyticsEvent> findAll() {
        return repo.findAll();
    }
    public List<AnalyticsEvent> findByRecipientId(Long receiverId) {
        return repo.findByReceiverId(receiverId);
    }
}
