package faang.school.analytics.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class GoalEventDto extends AbstractEventDto {

    @Override
    @JsonProperty("goalId")
    void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    @JsonProperty("userId")
    void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    @Override
    void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
