package faang.school.analytics.mapper.premium;

import faang.school.analytics.domain.dto.events.analytic.AnalyticsEventDto;
import faang.school.analytics.dto.premium.PremiumBoughtEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PremiumBoughtEventMapper {
    EventType recommendation_received = EventType.PREMIUM_BOUGHT;

    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(target = "eventTypeNumber", expression = "java(map(recommendation_received))")
    AnalyticsEventDto premiumBoughtToAnalyticsDto(PremiumBoughtEvent premiumBoughtEvent);

    @Named("mapToEventTypeNumber")
    default int map(EventType eventType) {
        return eventType.ordinal();
    }
}
