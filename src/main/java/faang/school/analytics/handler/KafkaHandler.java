package faang.school.analytics.handler;

import faang.school.analytics.dto.event.CommentEventDto;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaHandler {
    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(
        topics = "${kafka.topic.comment}",
        groupId = "${kafka.group-id.comment}",
        containerFactory = "commentEventConcurrentKafkaFactory")
    public void handleCommentCreated(CommentEventDto commentEventDto) {
        log.info("Received comment event: {}", commentEventDto);
        analyticsEventService.saveCreateComment(commentEventDto);
        log.debug("Comment event processed successfully");
    }
}
