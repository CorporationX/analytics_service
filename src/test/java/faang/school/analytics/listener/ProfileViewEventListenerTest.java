package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.redis.listener.ProfileViewEventListener;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileViewEventListenerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private ObjectMapper objectMapper;

    private ProfileViewEventListener profileViewEventListener;

    @BeforeEach
    void setUp() {
        profileViewEventListener = new ProfileViewEventListener(
                objectMapper,
                analyticsEventService,
                analyticsEventMapper,
                "testProfileViewChannel"
        );
    }

    @Test
    void testSaveEvent() {
        ProfileViewEventDto profileViewEventDto = new ProfileViewEventDto(1L, "name", 2L, "name", LocalDateTime.now());
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        when(analyticsEventMapper.toAnalyticsEvent(profileViewEventDto)).thenReturn(analyticsEvent);

        profileViewEventListener.saveEvent(profileViewEventDto);

        verify(analyticsEventMapper, times(1)).toAnalyticsEvent(profileViewEventDto);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
    }
}
