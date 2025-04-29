package faang.school.analytics.until;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EventType {
    LIKED_POST("likedPost");
    private final String key;
}