package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.CommentEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentEventListener {
    @Value("${spring.kafka.topics.commentTopic}")
    private String topic;

    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(topics = "${spring.kafka.topics.commentTopic}", groupId = "${spring.kafka.group-id}")
    public void listen(String message) throws JsonProcessingException {
        log.info("Received message from topic {}: {}", topic, message);
        CommentEventDto commentEventDto = objectMapper.readValue(message, CommentEventDto.class);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(commentEventDto);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}
