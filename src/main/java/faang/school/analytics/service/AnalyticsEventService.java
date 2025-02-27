package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;

    @Transactional
    public void saveViewPostEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
        log.info("Event #Post view# with id {} successfully saved", event.getId());
    }
}