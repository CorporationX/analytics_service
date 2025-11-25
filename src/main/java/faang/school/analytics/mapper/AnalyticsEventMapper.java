package faang.school.analytics.mapper;

import faang.school.analytics.dto.PremiumBoughtEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {

    AnalyticsEventMapper INSTANCE = Mappers.getMapper(AnalyticsEventMapper.class);

    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.PREMIUM_BOUGHT)")
    @Mapping(source = "purchaseDateTime", target = "receivedAt")
    @Mapping(target = "id", ignore = true)
    AnalyticsEvent toAnalyticsEvent(PremiumBoughtEvent premiumBoughtEvent);
}
