package faang.school.analytics.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@JsonIgnoreProperties({"postId"})
public class PostViewEventDto extends AbstractEventDto {
    @Override
    @JsonProperty("authorId")
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    @JsonProperty("viewerId")
    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    @Override
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
