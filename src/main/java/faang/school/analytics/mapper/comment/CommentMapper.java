package faang.school.analytics.mapper.comment;

import faang.school.analytics.dto.event.comment.CommentEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE , builder = @Builder(disableBuilder = true))
public abstract class CommentMapper {

  @Mapping(source = "commentId", target = "actorId")
  @Mapping(source = "postId", target = "receiverId")
  public abstract AnalyticsEvent toEvent(CommentEvent commentEvent);

  @AfterMapping
  protected void updateFields(@MappingTarget AnalyticsEvent event) {
    event.setEventType(EventType.POST_COMMENT);
  }

}
