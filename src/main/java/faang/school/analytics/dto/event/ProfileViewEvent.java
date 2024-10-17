package faang.school.analytics.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class ProfileViewEvent extends AbstractEventDto {

    @Override
    @JsonProperty("userId")
    void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    @JsonProperty("guestId")
    void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    @Override
    @JsonProperty("viewDateTime")
    void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
