package faang.school.analytics.handler;

import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.dto.event.FollowerEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FollowerEventHandlerTest {
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @InjectMocks
    private FollowerEventHandler followerEventHandler;
    private FollowerEvent followerEvent;
    private AnalyticsEventDto analyticsEventDto;

    @BeforeEach
    void setUp() {
        followerEvent = new FollowerEvent();
        analyticsEventDto = new AnalyticsEventDto();
    }

    @Test
    public void whenCanHandleThenTrue() {
        assertThat(followerEventHandler.canHandle(followerEvent)).isTrue();
    }


    @Test
    public void whenHandleThenSaveEvent() {
        when(analyticsEventMapper.fromFollowerEventToDto(followerEvent)).thenReturn(analyticsEventDto);
        followerEventHandler.handle(followerEvent);
        verify(analyticsEventService).save(analyticsEventDto);
    }
}