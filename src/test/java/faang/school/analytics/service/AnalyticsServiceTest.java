package faang.school.analytics.service;

import faang.school.analytics.dto.ProfileViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private AnalyticsEventMapper analyticsEventMapper = AnalyticsEventMapper.INSTANCE;
    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void profileViewEventSaveTest() {
        ProfileViewEvent profileViewEvent = new ProfileViewEvent();
        profileViewEvent.setUserProfileId(1L);
        profileViewEvent.setViewerId(2L);
        profileViewEvent.setViewDate(LocalDateTime.now());

        AnalyticsEvent analyticsEvent =
                analyticsEventMapper.fromProfileViewEventToAnalyticsEvent(profileViewEvent);
        analyticsEvent.setEventType(EventType.PROFILE_VIEW);

        when(analyticsEventRepository.save(any(AnalyticsEvent.class))).thenReturn(analyticsEvent);

        AnalyticsEvent result = analyticsService.profileViewEventSave(profileViewEvent);

        assertNotNull(result);
        assertEquals(EventType.PROFILE_VIEW, result.getEventType());
    }
}