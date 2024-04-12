package faang.school.analytics.services;

import faang.school.analytics.dto.ProfileViewEventDto;
import faang.school.analytics.mappers.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;

    public void gatherAnalyticsByProfileViews(AnalyticsEvent analyticsEvent){
        analyticsEventRepository.save(analyticsEvent);
        log.info("AnalyticsEvent successfully saved");
    }
}
