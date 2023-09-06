package faang.school.analytics.mapper;

import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostViewMapper {
    AnalyticsEvent ToAnalyticsEvent(PostViewEventDto postViewEventDto);
}
