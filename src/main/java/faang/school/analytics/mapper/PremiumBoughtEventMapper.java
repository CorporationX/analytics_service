package faang.school.analytics.mapper;

import faang.school.analytics.kafka.events.PremiumBoughtEvent;
import faang.school.analytics.model.AnalyticsPremiumBoughtEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PremiumBoughtEventMapper {

    @Mapping(source = "occurredAt", target = "receivedAt")
    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "eventTypeEnum", target = "eventType")
    @Mapping(target = "receiverId", ignore = true)
    @Mapping(target = "id", ignore = true)
    AnalyticsPremiumBoughtEvent toAnalyticsPremiumBoughtEvent(PremiumBoughtEvent premiumBoughtEvent);
}
