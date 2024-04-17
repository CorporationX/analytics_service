package faang.school.analytics.mapper;

import faang.school.analytics.dto.SearchAppearanceEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(source = "viewedUserId", target = "receiverId")
    @Mapping(source = "viewerUserId", target = "actorId")
    @Mapping(source = "viewingTime", target = "receivedAt")
    AnalyticsEvent toEntity(SearchAppearanceEvent searchAppearanceEvent);
}
