package faang.school.analytics.dtoForRedis;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowerEventDto {

    private Long visitorId;
    private Long visitedId;
    private Long projectId;
    private LocalDateTime subscribedDateTime;
}
