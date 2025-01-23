package faang.school.analytics.service;

import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.mapper.AnalyticsEventMapper;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowerEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public void save(AnalyticsEventDto analyticsEventDto){
        log.info("Save AnalyticsEventDto: {}", analyticsEventDto);
        analyticsEventRepository.save(analyticsEventMapper.toEntity(analyticsEventDto));
    }
}