package faang.school.analytics.dto.event.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import faang.school.analytics.dto.event.AbstractEvent;

import java.time.LocalDateTime;

public class ProjectViewEvent extends AbstractEvent {

    @Override
    @JsonProperty("userId")
    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    @Override
    @JsonProperty("projectId")
    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    @JsonProperty("viewTime")
    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }
}
