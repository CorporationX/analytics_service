package faang.school.analytics.dto.user;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SearchAppearanceEvent(long receiverId, long actorId, LocalDateTime receivedAt) {
}