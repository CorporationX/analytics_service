package faang.school.analytics.listener;

import faang.school.analytics.dto.HashtagRequestEvent;
import faang.school.analytics.mapper.HashtagEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HashtagRequestEventListener {

    private final AbstractEventListener eventListener;
    private final HashtagEventMapper hashtagEventMapper;

    @KafkaListener(
            topics = "${spring.kafka.topics.hashtag-analytics.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void receive(String message) {
        eventListener.processEvent(message, HashtagRequestEvent.class, hashtagEventMapper::toDto);
    }
}
