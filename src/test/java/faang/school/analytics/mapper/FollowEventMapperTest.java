package faang.school.analytics.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import faang.school.analytics.event.FollowerEvent;
import faang.school.analytics.model.AnalyticsEvent;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class FollowEventMapperTest {

    private final FollowEventMapper mapper = Mappers.getMapper(FollowEventMapper.class);

    @Test
    void toEntity_mapsAllFieldsCorrectly() {
        FollowerEvent dto = new FollowerEvent();
        dto.setFollowerId(10L);
        dto.setTargetId(20L);
        LocalDateTime ts = LocalDateTime.of(2025, 3, 17, 12, 34, 56);
        dto.setTimestamp(ts);

        AnalyticsEvent entity = mapper.toEntity(dto);

        assertThat(entity.getReceivedAt()).isEqualTo(ts);
    }

}
