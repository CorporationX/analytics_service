package faang.school.analytics.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class FollowerEventDto extends AbstractEventDto {

    @Override
    @JsonProperty("followeeId")
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    @JsonProperty("followerId")
    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    @Override
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
