package faang.school.analytics.mapper;

import faang.school.analytics.service.redis.events.UserEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserEventMapper {
    UserEvent readValue(byte[] messageBody, Class targetClass);
}
