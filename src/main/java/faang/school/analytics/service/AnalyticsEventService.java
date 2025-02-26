package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnalyticsEventService {
    private final AnalyticsEventRepository repository;

    public void saveViewPostEvent(AnalyticsEvent event) {
        repository.save(event);
        log.info("Event #Post view# with id {} save to repository", event.getId());
    }
}