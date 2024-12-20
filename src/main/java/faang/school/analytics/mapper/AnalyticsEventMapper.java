package faang.school.analytics.mapper;

import faang.school.analytics.dto.MentorshipRequestEvent;
import faang.school.analytics.dto.event.AdBoughtEvent;
import faang.school.analytics.dto.event.AnalyticsEventResponseDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventResponseDto entityToResponseDto(AnalyticsEvent event);

    AnalyticsEvent toEntity(AnalyticsEventResponseDto dto);

    default AnalyticsEvent dtoToEntity(AdBoughtEvent event) {
        return AnalyticsEvent.builder()
                .id(event.getPostId())
                .actorId(event.getActorId())
                .receiverId(event.getReceiverId())
                .paymentAmount(event.getPaymentAmount())
                .adDuration(event.getAdDuration())
                .receivedAt(event.getReceivedAt())
                .build();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "time", target = "receivedAt")
    AnalyticsEvent toAnalyticsEventMentorshipRequest(MentorshipRequestEvent mentorshipRequestEvent);
}
