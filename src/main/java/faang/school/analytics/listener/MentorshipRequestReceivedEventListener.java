package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.MentorshipRequestReceivedDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Slf4j
@Service
public class MentorshipRequestReceivedEventListener extends AbstractEventListener<MentorshipRequestReceivedDto> {
    public MentorshipRequestReceivedEventListener(ObjectMapper objectMapper,
                                                  AnalyticsEventService analyticsEventService,
                                                  AnalyticsEventMapper mapper) {
        super(objectMapper, analyticsEventService, mapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        Function<MentorshipRequestReceivedDto, AnalyticsEvent> toEventFunction =
                eventDto -> getMapper().mentorshipRequestReceivedDtoToAnalyticsEvent(eventDto);
        saveEvent(message, MentorshipRequestReceivedDto.class, toEventFunction);
    }
}
