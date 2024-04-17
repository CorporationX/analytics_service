package faang.school.analytics.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.SearchAppearanceEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SearchAppearanceEventListener extends AbstractListener<SearchAppearanceEvent> {

    public SearchAppearanceEventListener(ObjectMapper objectMapper,
                                         AnalyticsEventMapper analyticsEventMapper,
                                         AnalyticsEventRepository repository) {
        super(objectMapper, analyticsEventMapper, repository);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        SearchAppearanceEvent event = readValue(message.getBody(), SearchAppearanceEvent.class);
        log.info("Received was a message about viewing the user's profile: {}", message);
        save(analyticsEventMapper.toEntity(event));
    }
}