//package faang.school.analytics.mapper;
//
//import faang.school.analytics.dto.AnalyticsEventDto;
//import faang.school.analytics.dto.CommentEventDto;
//import faang.school.analytics.model.AnalyticsEvent;
//import org.mapstruct.InjectionStrategy;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.ReportingPolicy;
//import org.springframework.stereotype.Component;
//import org.yaml.snakeyaml.events.CommentEvent;
//
//@Component
//@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
//public interface CommentEventMapper {
////    @Mapping(source = "postId", target = "receiverId")
////    @Mapping(source = "authorId", target = "actorId")
////    @Mapping(source = "createdAt", target = "receivedAt")
////    AnalyticsEvent toEntity(CommentEventDto commentEventDto);
////
////    @Mapping(source = "receiverId", target = "postId")
////    @Mapping(source = "actorId", target = "authorId")
////    @Mapping(source = "receivedAt", target = "createdAt")
////    AnalyticsEventDto toDto(CommentEventDto commentEvent);
//
//
//    @Mapping(source = "authorId", target = "actorId")
//    @Mapping(source = "createdAt", target = "receivedAt")
//    AnalyticsEvent toEntity(CommentEventDto commentEventDto);
//
//
//    @Mapping(source = "actorId", target = "authorId")
//    @Mapping(source = "receivedAt", target = "createdAt")
//    CommentEventDto toDto(AnalyticsEvent commentEvent);
//}