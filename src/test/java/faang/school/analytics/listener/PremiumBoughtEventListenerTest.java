package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.premium.PremiumBoughtEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.redis.listener.PremiumBoughtEventListener;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PremiumBoughtEventListenerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private ObjectMapper objectMapper;

    private PremiumBoughtEventListener premiumBoughtEventListener;

    @BeforeEach
    void setUp() {
        premiumBoughtEventListener = new PremiumBoughtEventListener(
                objectMapper,
                analyticsEventService,
                analyticsEventMapper,
                "testPremiumBoughtChannel"
        );
    }

    @Test
    void testSaveEvent() {
        PremiumBoughtEventDto premiumBoughtEventDto = new PremiumBoughtEventDto();
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        when(analyticsEventMapper.toAnalyticsEvent(premiumBoughtEventDto)).thenReturn(analyticsEvent);

        premiumBoughtEventListener.saveEvent(premiumBoughtEventDto);

        verify(analyticsEventMapper, times(1)).toAnalyticsEvent(premiumBoughtEventDto);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
    }
}
