package faang.school.analytics.service;

import faang.school.analytics.dto.followEvent.FollowEventDto;
import faang.school.analytics.mapper.FollowEventMapper;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final FollowEventMapper followEventMapper;

    public void followEventSave(FollowEventDto followEventDto) {
        var event = followEventMapper.toEntity(followEventDto);
        analyticsEventRepository.save(event);
    }
}
