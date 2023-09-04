package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.MentorshipRequestedEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;



@Component
@Slf4j
public class MentorshipRequestedEventListener extends AbstractListener<MentorshipRequestedEventDto> {
    public MentorshipRequestedEventListener(ObjectMapper objectMapper,
                                            AnalyticsEventMapper analyticsEventMapper,
                                            AnalyticsEventRepository repository) {
        super(objectMapper, analyticsEventMapper, repository);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipRequestedEventDto mentorshipRequestedEventDto = readValue(message.getBody(), MentorshipRequestedEventDto.class);
        log.info("Received new mentorship requested event message: {}", mentorshipRequestedEventDto);
        save(analyticsEventMapper.toMentorshipRequestedEntity(mentorshipRequestedEventDto));
    }
}
