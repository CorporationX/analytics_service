package faang.school.analytics.mapper;

import faang.school.analytics.dto.PremiumBoughtEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PremiumBoughtMapperTest {
    @Spy
    private PremiumBoughtMapperImpl premiumBoughtMapper;

    @Test
    void toAnalyticsEvent() {
        PremiumBoughtEvent eventDto = PremiumBoughtEvent.builder()
                .receiverId(1L)
                .amountPayment(10)
                .daysSubscription(30)
                .build();
        AnalyticsEvent eventEntity = new AnalyticsEvent();
        eventEntity.setReceiverId(1L);

        AnalyticsEvent actualEvent = premiumBoughtMapper.toAnalyticsEvent(eventDto);
        assertEquals(eventEntity, actualEvent);
    }

}