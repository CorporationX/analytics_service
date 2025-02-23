package faang.school.analytics.service;

import faang.school.analytics.dto.ProfileViewEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Test
    void addEvent() {
        ProfileViewEventDto eventDto = ProfileViewEventDto.builder()
                .viewerId(2L)
                .profileOwnerId(1L)
                .viewedAt(LocalDateTime.now())
                .build();

        AnalyticsEvent event = AnalyticsEvent.builder()
                .receiverId(eventDto.getProfileOwnerId())
                .actorId(eventDto.getViewerId())
                .eventType(EventType.PROFILE_VIEW)
                .receivedAt(eventDto.getViewedAt())
                .build();

        analyticsEventService.addEvent(event);

        Mockito.verify(analyticsEventRepository, times(1)).save(event);
    }
}