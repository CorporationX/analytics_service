package faang.school.analytics.redis.listener;

import faang.school.analytics.dto.user.premium.PremiumBoughtEventDto;
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
class PremiumBoughtEventListenerTest {
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private PremiumBoughtEventListener premiumBoughtEventListener;

    @Test
    @DisplayName("Save event successful")
    void testSaveEventSuccessful() {
        PremiumBoughtEventDto eventDto = mock(PremiumBoughtEventDto.class);
        AnalyticsEvent event = mock(AnalyticsEvent.class);
        when(analyticsEventMapper.toAnalyticsEvent(eventDto)).thenReturn(event);

        premiumBoughtEventListener.saveEvent(eventDto);

        verify(analyticsEventService).saveEvent(event);
    }

    @Test
    @DisplayName("Get event type successful")
    void testGetEventTypeSuccessful() {
        assertThat(premiumBoughtEventListener.getEventType())
                .isEqualTo(PremiumBoughtEventDto.class);
    }

    @Test
    @DisplayName("Get event type name successful")
    void testGetEventTypeNameSuccessful() {
        assertThat(premiumBoughtEventListener.getEventTypeName())
                .isEqualTo("Premium bought events");
    }
}