package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsEventMapper {

    public static AnalyticsDto toDto(AnalyticsEvent entity) {
        return new AnalyticsDto(
                entity.getId(),
                entity.getReceiverId(),
                entity.getActorId(),
                entity.getEventType(),
                entity.getReceivedAt()
        );
    }

    public static AnalyticsEvent toEntity(AnalyticsDto dto) {
        AnalyticsEvent entity = new AnalyticsEvent();
        entity.setEventType(dto.getEventType());
        entity.setReceiverId(dto.getReceiverId());
        entity.setActorId(dto.getActorId());
        entity.setEventType(dto.getEventType());
        entity.setReceivedAt(dto.getReceivedAt());

        return entity;

    }
}