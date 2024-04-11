package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;

    @Transactional
    public void saveEvent(AnalyticsEventDto analyticsEventDto) {
        AnalyticsEvent savedEvent = analyticsEventRepository.save(analyticsEventMapper.toEntity(analyticsEventDto));
        log.info("Event saved: {}", savedEvent);
    }
}
