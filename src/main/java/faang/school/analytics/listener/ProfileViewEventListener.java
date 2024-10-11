package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.service.analytics.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileViewEventListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @EventListener
    public void handleMessage(String event) {
        ProfileViewEventDto eventDto = readAndValidationValue(event);
        analyticsEventService.saveEvent(eventDto);
    }

    private ProfileViewEventDto readAndValidationValue(String event) {
        try {
            return objectMapper.readValue(event, ProfileViewEventDto.class);
        } catch (JsonProcessingException exception) {
            log.error("Error parsing profile view event: {}", event, exception);
            throw new RuntimeException(exception);
        }
    }
}
