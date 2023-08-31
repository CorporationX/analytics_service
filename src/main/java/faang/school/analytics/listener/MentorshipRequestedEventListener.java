package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.MentorshipRequestedEventDto;
import faang.school.analytics.mapper.MentorshipRequestedEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class MentorshipRequestedEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final MentorshipRequestedEventMapper mentorshipRequestedEventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipRequestedEventDto mentorshipRequestedEventDto = fromJson(message.getBody(), MentorshipRequestedEventDto.class);
        AnalyticsEventDto analyticsEventDto = mentorshipRequestedEventMapper.toDto(mentorshipRequestedEventDto);
           analyticsEventDto.setEventType(EventType.MENTORSHIP_REQUESTED);
           analyticsEventRepository.save(mentorshipRequestedEventMapper.toEntityFromAnalyticsEventDto(analyticsEventDto));
    }
     public <T> T fromJson(byte[] json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (IOException e) {
            log.error("Error parsing json", e);
        }
        return null;
    }
}
