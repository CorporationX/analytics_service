package faang.school.analytics.service;

import faang.school.analytics.dto.followEvent.FollowEventDto;
import faang.school.analytics.mapper.FollowEventMapper;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final FollowEventMapper followEventMapper;

    @Transactional
    public void followEventSave(FollowEventDto followEventDto) {
        var event = followEventMapper.toEntity(followEventDto);
        analyticsEventRepository.save(event);
    }

    public void save(Message message, byte[] pattern) {
        CommentEventDto commentEventDto = null;
        try {
            commentEventDto = objectMapper.readValue(message.getBody(), CommentEventDto.class);
            AnalyticsEvent analyticsEvent = eventMapper.toEntity(commentEventDto);
            analyticsEvent.setReceivedAt(LocalDateTime.now());
            analyticsEvent.setEventType(POST_COMMENT);
            analyticsEventRepository.save(analyticsEvent);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
