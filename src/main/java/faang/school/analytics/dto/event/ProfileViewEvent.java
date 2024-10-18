package faang.school.analytics.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class ProfileViewEvent extends AbstractEventDto {

    @Override
    @JsonProperty("userId")
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    @JsonProperty("guestId")
    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    @Override
    @JsonProperty("viewDateTime")
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
