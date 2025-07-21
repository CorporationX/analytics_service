package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository repository;
    private final AnalyticsEventMapper mapper;

    public void processCommentEvent(AnalyticsEvent analyticsEvent) {
        AnalyticsDto dto = AnalyticsDto.builder()
                .postId(analyticsEvent.getId())
                .receiverId(analyticsEvent.getReceiverId())
                .actorId(analyticsEvent.getActorId())
                .eventType(EventType.POST_COMMENT)
                .receivedAt(analyticsEvent.getReceivedAt())
                .build();

        AnalyticsEvent event = AnalyticsEventMapper.toEntity(dto);
        repository.save(event);
    }
}