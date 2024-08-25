package faang.school.analytics.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class FollowerEventDto extends AbstractEventDto {
    private long followerId;
    private long followeeId;
    private LocalDateTime eventTime;
}
