package faang.school.analytics.dto.event.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import faang.school.analytics.dto.event.AbstractEvent;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class PostViewEvent extends AbstractEvent {

    private Long postId;

    @Override
    @JsonProperty("userId")
    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    @Override
    @JsonProperty("authorId")
    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    @JsonProperty("viewTime")
    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }
}
