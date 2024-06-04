package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    private static final Long ACTOR_ID = 1L;
    private static final Long RECEIVER_ID = 2L;
    private static final Long EVENT_ID = 1L;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    public void whenEventSave() {
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .actorId(ACTOR_ID)
                .actorId(RECEIVER_ID)
                .build();
        AnalyticsEvent expectedEvent = AnalyticsEvent.builder()
                .id(EVENT_ID)
                .actorId(ACTOR_ID)
                .actorId(RECEIVER_ID)
                .build();

        when(analyticsEventRepository.save(analyticsEvent)).thenReturn(expectedEvent);

        analyticsEventService.saveEvent(analyticsEvent);

        verify(analyticsEventRepository).save(analyticsEvent);
    }

}