package faang.school.analytics.mapper;

import faang.school.analytics.dto.PremiumBoughtEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
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
        PremiumBoughtEventDto eventDto = PremiumBoughtEventDto.builder()
                .receiverId(1L)
                .amountPayment(10)
                .daysSubscription(30)
                .build();
        AnalyticsEvent eventEntity = new AnalyticsEvent();
        eventEntity.setReceiverId(1L);

        AnalyticsEvent actualEvent = premiumBoughtMapper.toAnalyticsEvent(eventDto);
        assertEquals(eventEntity, actualEvent);
    }

    @Test
    void successMappingToPremiumBoughtEventDto() {
        AnalyticsEvent eventEntity = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(1L)
                .actorId(1L)
                .eventType(EventType.PREMIUM_BOUGHT)
                .build();
        PremiumBoughtEventDto eventDto = new PremiumBoughtEventDto();
        eventDto.setReceiverId(1L);

        PremiumBoughtEventDto actualEventDto = premiumBoughtMapper.toPremiumBoughtEventDto(eventEntity);
        assertEquals(eventDto, actualEventDto);
    }
}