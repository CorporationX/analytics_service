package faang.school.analytics.listener.data;

import faang.school.analytics.dto.subscription.FollowerEventDto;
import faang.school.analytics.listener.FollowerEventListener;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class FollowerEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private FollowerEventDto followerEvent;

    @InjectMocks
    private FollowerEventListener followerEventListener;

    @Test
    @DisplayName("Проверка на успешное выполенение метода handleEvent")
    void givenValidData_whenHandleEvent_thenSuccess() {
        followerEvent.setFolloweeId(1L);
        followerEvent.setFollowerId(2L);

        followerEventListener.handleEvent(followerEvent);
    }
}