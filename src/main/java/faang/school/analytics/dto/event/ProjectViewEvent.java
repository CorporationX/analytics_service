package faang.school.analytics.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ProjectViewEvent extends AbstractEvent {

    @Override
    @JsonProperty("userId")
    public void setActorId(long actorId) {
        super.setActorId(actorId);
    }

    @Override
    @JsonProperty("projectId")
    public void setReceiverId(long receiverId) {
        super.setReceiverId(receiverId);
    }

    @Override
    @JsonProperty("viewTime")
    public void setReceivedAt(LocalDateTime receivedAt) {
        super.setReceivedAt(receivedAt);
    }
}
