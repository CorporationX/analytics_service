package faang.school.analytics.dto.event.type.service.post.like;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostLikeEventDto extends AbstractLikeEventDto {
    private final long postId;

    @JsonCreator
    @Builder
    public PostLikeEventDto(
            @JsonProperty("postId") @Positive Long postId,
            @JsonProperty("authorId") @Positive Long authorId,
            @JsonProperty("userExciterId") @Positive Long userExciterId,
            @JsonProperty("createdAt") LocalDateTime createdAt) {
        super(EventType.POST_LIKE, authorId, userExciterId, createdAt);
        this.postId = postId;
    }
}