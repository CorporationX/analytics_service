package faang.school.analytics.event;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class SearchAppearanceEvent {

    private Long userId;
    private Long searchingUserId;
    private LocalDateTime viewedAt;

}