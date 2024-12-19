package faang.school.analytics.redis.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileViewEvent {

    private long id;

    private long viewerID;

    private LocalDateTime viewingDateAndTime;
}