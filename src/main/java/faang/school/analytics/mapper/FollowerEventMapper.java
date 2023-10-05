package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface FollowerEventMapper {
    @Mapping(target = "actorId", source = "followerId")
    @Mapping(target = "receiverId", source = "followeeId")
    @Mapping(source = "receivedAt", target = "subscriptionTime", qualifiedByName = "zoneToLocalDateTime")
    AnalyticsEvent toModel(FollowerEventDto eventDto);

    @Mapping(target = "actorId", source = "followerId")
    @Mapping(target = "receiverId", source = "followeeId")
    @Mapping(source = "receivedAt", target = "subscriptionTime", qualifiedByName = "localToZoneDateTime")
    AnalyticsEventDto toDto(FollowerEventDto eventDto);

        @Named("zoneToLocalDateTime")
        default LocalDateTime zoneToLocalDateTime(ZonedDateTime zonedDateTime){
            return zonedDateTime.toLocalDateTime();
        }

        @Named("localToZoneDateTime")
        default ZonedDateTime localToZoneDateTime(LocalDateTime localDateTime){
            ZoneId zoneId = ZoneId.of("UTC");
            return localDateTime.atZone(zoneId);
        }
    }
