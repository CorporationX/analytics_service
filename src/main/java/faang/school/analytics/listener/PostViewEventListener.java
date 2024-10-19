package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Slf4j
@Service
public class PostViewEventListener extends AbstractEventListener<PostViewEventDto> {
    public PostViewEventListener(ObjectMapper objectMapper,
                                 AnalyticsEventService analyticsEventService,
                                 AnalyticsEventMapper mapper) {
        super(objectMapper, analyticsEventService, mapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        Function<PostViewEventDto, AnalyticsEvent> toEventFunction =
                eventDto -> getMapper().postViewEventDtoToAnalyticsEvent(eventDto);
        saveEvent(message, PostViewEventDto.class, toEventFunction);
    }
}
