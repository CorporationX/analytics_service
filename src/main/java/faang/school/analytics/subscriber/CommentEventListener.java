package faang.school.analytics.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentEventListener implements MessageListener {
    private final AnalyticsEventRepository repository;
    private final AnalyticsEventMapper mapper;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEvent commentEvent;
        try {
            commentEvent = objectMapper.readValue(message.getBody(), CommentEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("получили ивент: " + commentEvent);
        repository.save(mapper.toAnalytics(commentEvent));
    }
}
