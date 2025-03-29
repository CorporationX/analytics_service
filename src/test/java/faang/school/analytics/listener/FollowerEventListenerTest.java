package faang.school.analytics.listener;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FollowerEventListenerTest {

    @Mock
    private AnalyticsEventService analyticsEventService;
    private AnalyticsEventMapper analyticsEventMapper;
    private FollowerEventListener listener;

    @BeforeEach
    void setUp() {
        analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);
        listener = new FollowerEventListener(analyticsEventService, analyticsEventMapper);
    }

    @Test
    void listen_SaveEventSuccessfully() {
        FollowerEvent followerEvent = new FollowerEvent(1L, 2L, null, LocalDateTime.now());
        AnalyticsEventDto analyticsEventDto = analyticsEventMapper.toDto(followerEvent);
        listener.listen(followerEvent);
        Mockito.verify(analyticsEventService, times(1)).saveEvent(analyticsEventDto);
    }

    @Test
    void listen_ShouldNotCrashWhenSaveNullEvent() {
        listener.listen(null);
        Mockito.verify(analyticsEventService, times(1)).saveEvent(null);
    }
}
