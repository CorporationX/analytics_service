package faang.school.analytics.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class LikeEventDto extends AbstractEventDto {

    @Override
    @JsonProperty("postAuthorId")
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    @JsonProperty("likeAuthorId")
    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    @Override
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}