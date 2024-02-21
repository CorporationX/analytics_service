package faang.school.analytics.controller;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.PremiumBoughtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PremiumBoughtControllerTest {
    @InjectMocks
    private PremiumBoughtController premiumBoughtController;
    @Mock
    private PremiumBoughtService premiumBoughtService;

    @Test
    void successGetAnalytics() {
        AnalyticsEvent event = AnalyticsEvent.builder()
                .id(1L)
                .actorId(1L)
                .receiverId(1L)
                .build();
        long receiverId = event.getReceiverId();
        String type = "premium_bought";
        EventType eventType = EventType.valueOf(type.toUpperCase(Locale.ROOT));

        premiumBoughtController.getAnalytics(receiverId, type);
        verify(premiumBoughtService, times(1)).getAnalytics(receiverId, eventType);
    }
}