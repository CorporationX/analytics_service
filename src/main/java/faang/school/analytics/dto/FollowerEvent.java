package faang.school.analytics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerEvent {

    @Positive
    @NotNull
    private Long followerId;
    private Long followeeId;
    private Long projectId;

    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime eventTime;

    public FollowerEvent(long followerId, long followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
        eventTime = LocalDateTime.now();
    }

}