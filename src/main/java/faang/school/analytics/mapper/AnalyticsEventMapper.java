package faang.school.analytics.mapper;

import faang.school.analytics.dto.analyticsEvent.AdBoughtEventResponseDto;
import faang.school.analytics.dto.analyticsEvent.AnalyticsEventResponseDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEventResponseDto entityToResponseDto(AnalyticsEvent event);

    default AnalyticsEvent dtoToEntity(AdBoughtEventResponseDto event) {
        return AnalyticsEvent.builder()
                .id(event.getPostId())
                .actorId(event.getActorId())
                .receiverId(event.getActorId())
                .paymentAmount(event.getPaymentAmount())
                .adDuration(event.getAdDuration())
                .receivedAt(event.getReceivedAt())
                .build();
    }
}
