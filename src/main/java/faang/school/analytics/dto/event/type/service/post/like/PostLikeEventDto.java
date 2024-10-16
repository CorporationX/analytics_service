package faang.school.analytics.dto.event.type.service.post.like;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDateTime;

public class PostLikeEventDto extends AbstractLikeEventDto {
    @JsonCreator
    @Builder
    public PostLikeEventDto(
            @JsonProperty("likedEntityId") @Positive Long likedEntityId,
            @JsonProperty("authorId") @Positive Long authorId,
            @JsonProperty("userExciterId") @Positive Long userExciterId,
            @JsonProperty("createdAt") LocalDateTime createdAt) {
        super(likedEntityId, authorId, userExciterId, createdAt);
    }
}