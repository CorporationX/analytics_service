package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.redis.listener.ProfileViewListEventListener;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ProfileViewListEventListenerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private ObjectMapper objectMapper;

    private ProfileViewListEventListener profileViewListEventListener;

    @BeforeEach
    void setUp() {
        profileViewListEventListener = new ProfileViewListEventListener(
                objectMapper,
                analyticsEventMapper,
                analyticsEventService,
                "testProfileViewListChannel"
        );
    }

    @Test
    void testSaveEvents() {
        List<ProfileViewEventDto> profileViewEventDtos = List.of(
                new ProfileViewEventDto(1L, "name", 2L, "name", LocalDateTime.now()),
                new ProfileViewEventDto(3L, "name", 4L, "name", LocalDateTime.now()),
                new ProfileViewEventDto(5L, "name", 6L, "name", LocalDateTime.now())
        );
        List<AnalyticsEvent> analyticsEvents = List.of(new AnalyticsEvent(), new AnalyticsEvent());

        when(analyticsEventMapper.toAnalyticsEvents(profileViewEventDtos)).thenReturn(analyticsEvents);

        profileViewListEventListener.saveEvents(profileViewEventDtos);

        verify(analyticsEventMapper, times(1)).toAnalyticsEvents(profileViewEventDtos);
        verify(analyticsEventService, times(1)).saveAllEvents(analyticsEvents);
    }
}
