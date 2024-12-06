package faang.school.analytics.service;

import faang.school.analytics.event.ProfileViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Test
    public void testSaveProfileView() {
        // arrange
        long actorId = 5L;
        long receiverId = 2L;
        LocalDateTime receivedAt = LocalDateTime.now();
        ProfileViewEvent profileViewEvent = ProfileViewEvent.builder()
                .actorId(actorId)
                .receiverId(receiverId)
                .receivedAt(receivedAt)
                .build();

        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .actorId(actorId)
                .receiverId(receiverId)
                .receivedAt(receivedAt)
                .eventType(EventType.PROFILE_VIEW)
                .build();

        when(analyticsEventMapper.toAnalyticsEvent(profileViewEvent))
                .thenReturn(analyticsEvent);

        // act
        analyticsEventService.saveProfileView(profileViewEvent);

        // assert
        verify(analyticsEventRepository).save(analyticsEvent);
    }
}
