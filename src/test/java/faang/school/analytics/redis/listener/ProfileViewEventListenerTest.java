package faang.school.analytics.redis.listener;

import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileViewEventListenerTest {
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private ProfileViewEventListener profileViewEventListener;

    @Test
    @DisplayName("Save event successful")
    void testSaveEventSuccessful() {
        ProfileViewEventDto eventDto = mock(ProfileViewEventDto.class);
        AnalyticsEvent event = mock(AnalyticsEvent.class);
        when(analyticsEventMapper.toAnalyticsEvent(eventDto)).thenReturn(event);

        profileViewEventListener.saveEvent(eventDto);

        verify(analyticsEventService).saveEvent(event);
    }

    @Test
    @DisplayName("Get event type successful")
    void testGetEventTypeSuccessful() {
        assertThat(profileViewEventListener.getEventType())
                .isEqualTo(ProfileViewEventDto.class);
    }

    @Test
    @DisplayName("Get event type name successful")
    void testGetEventTypeNameSuccessful() {
        assertThat(profileViewEventListener.getEventTypeName())
                .isEqualTo("Profile view events");
    }
}