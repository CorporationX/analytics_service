package faang.school.analytics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.mapper.EventMapper;
import faang.school.analytics.messaging.FollowerEventListener;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

import static faang.school.analytics.model.EventType.POST_COMMENT;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventRepository analyticsEventRepository;
    private final EventMapper eventMapper;

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
