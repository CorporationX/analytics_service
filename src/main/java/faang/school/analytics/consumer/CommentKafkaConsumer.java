package faang.school.analytics.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CommentKafkaConsumer {

    @KafkaListener(topics = "${spring.kafka.comment_create_event_topic_name}", groupId = "group-id")
    public void onCommentCreate(String message) {
        System.out.println("Received message: " + message);
    }
}
