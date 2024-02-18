package faang.school.analytics.listner;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.mapper.base.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ProjectViewListener extends AbstractEventListener<ProjectViewEvent> implements MessageListener {
    private final ObjectMapper objectMapper;

    public ProjectViewListener(ObjectMapper objectMapper,  AnalyticsEventService analyticsEventService, List<AnalyticsEventMapper<ProjectViewEvent>> analyticsEventMappers) {
        super(objectMapper, analyticsEventService, analyticsEventMappers);
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String s;
        try {
             s = objectMapper.readValue(message.getBody(), String.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(s);
    }
}
