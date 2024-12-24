package faang.school.analytics.listener;

import faang.school.analytics.event.UserSearchAppearanceEvent;
import faang.school.analytics.mapper.event.UserSearchAppearanceEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.event.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserSearchAppearanceEventListenerTest {
    @Spy
    private UserSearchAppearanceEventMapperImpl userSearchAppearanceEventMapper = new UserSearchAppearanceEventMapperImpl();
    @Mock
    private AnalyticsEventService analyticsEventService;
    @InjectMocks
    private UserSearchAppearanceEventListener listener;

    @Test
    void testHandleEventShouldMapAndAddAnalyticsEvent() {
        long searchedUserId = 1L;
        long userWhoSearchId = 2L;
        LocalDateTime searchTime = LocalDateTime.now();
        UserSearchAppearanceEvent userSearchAppearanceEvent = new UserSearchAppearanceEvent(
                searchedUserId,
                userWhoSearchId,
                searchTime
        );
        ArgumentCaptor<AnalyticsEvent> analyticsEventCaptor = ArgumentCaptor.forClass(AnalyticsEvent.class);

        listener.handleEvent(userSearchAppearanceEvent);

        verify(analyticsEventService).addNewEvent(analyticsEventCaptor.capture());
        AnalyticsEvent analyticsEvent = analyticsEventCaptor.getValue();
        assertEquals(searchedUserId, analyticsEvent.getReceiverId());
        assertEquals(userWhoSearchId, analyticsEvent.getActorId());
        assertEquals(searchTime, analyticsEvent.getReceivedAt());
        assertEquals(EventType.PROFILE_APPEARED_IN_SEARCH, analyticsEvent.getEventType());
    }
}
