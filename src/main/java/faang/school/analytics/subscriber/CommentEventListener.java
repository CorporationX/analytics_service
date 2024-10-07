package faang.school.analytics.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentEventListener implements MessageListener {
    private final AnalyticsEventRepository repository;
    private final AnalyticsEventMapper mapper;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEvent commentEvent;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            String json = (String) new ObjectInputStream(new ByteArrayInputStream(message.getBody())) .readObject();
            commentEvent = objectMapper.readValue(json, CommentEvent.class);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        log.info(commentEvent.toString());
        repository.save(mapper.toAnalytics(commentEvent));
    }
}
