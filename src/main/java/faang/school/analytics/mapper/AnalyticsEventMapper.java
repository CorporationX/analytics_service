package faang.school.analytics.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.subscription.SubscriptionEvent;
import faang.school.analytics.model.subscription.SubscriptionEventType;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEventDto toDto(AnalyticsEvent event);
    
    @Mapping(source = "followerId", target = "actorId")
    @Mapping(source = "followeeId", target = "receiverId")
    @Mapping(source = "eventType", target = "eventType", qualifiedByName = "convertSubscriptionEventType")
    @Mapping(source = "createdAt", target = "receivedAt")
    AnalyticsEvent toModel(SubscriptionEvent dto);
    
    @Named("convertSubscriptionEventType")
    default EventType convertSubscriptionEventType(SubscriptionEventType eventType) {
        return EventType.valueOf(eventType.name());
    }
}
