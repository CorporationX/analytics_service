package faang.school.analytics.messaging.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.comment.CommentEvent;
import faang.school.analytics.mapper.comment.CommentMapper;
import faang.school.analytics.messaging.listener.AbstractEventListener;
import faang.school.analytics.service.analytics.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEvent> implements
    MessageListener {

  private final CommentMapper commentMapper;

  public CommentEventListener(ObjectMapper objectMapper,
      AnalyticsEventService analyticsEventService, CommentMapper commentMapper) {
    super(objectMapper, analyticsEventService);
    this.commentMapper = commentMapper;
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    handleEvent(message, CommentEvent.class,
        event -> persistAnalyticsData(commentMapper.toEvent(event)));
  }
}
