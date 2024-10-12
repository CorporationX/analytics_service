package faang.school.analytics.mapper;


import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.redis.MentorshipRequestEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticMapper {

    @Mapping(source = "requesterId", target = "actorId")
    @Mapping(source = "requestTime", target = "receivedAt")
    AnalyticsEvent toAnalytic(MentorshipRequestEvent event);
}
