package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.NonNull;
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
    public void saveViewPostEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
        log.info("Event #Post view# with id {} successfully saved", event.getId());
    public void addEvent(@NonNull AnalyticsEvent event) {

        AnalyticsEvent analyticsEventSaved = analyticsEventRepository.save(event);
        log.info("Added event: {} ", analyticsEventSaved);
    }
}