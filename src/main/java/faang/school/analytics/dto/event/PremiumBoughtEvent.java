package faang.school.analytics.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.time.LocalDateTime;

public class PremiumBoughtEvent extends AbstractEventDto{
    @Override
    public void setReceiverId(Long receiverId) {
        this.receiverId = 0L;
    }

    @Override
    @JsonProperty("userId")
    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    @Override
    @JsonProperty("timestamp")
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}