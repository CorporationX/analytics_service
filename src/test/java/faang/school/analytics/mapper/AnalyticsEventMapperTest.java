package faang.school.analytics.mapper;

import faang.school.analytics.dto.redis.PostViewEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventMapperTest {

    @InjectMocks
    private AnalyticsEventMapperImpl mapper;

    @Test
    public void testToEntity() {
        PostViewEventDto dto = new PostViewEventDto();
        LocalDateTime createdAt = LocalDateTime.parse("2023-08-24T12:34:56");

        dto.setCreatedAt(createdAt);
        dto.setUserId(1L);
        dto.setAuthorId(2L);

        AnalyticsEvent entity = mapper.toEntity(dto);

        assertEquals(createdAt, entity.getReceivedAt());
        assertEquals(1L, entity.getActorId());
        assertEquals(2L, entity.getReceiverId());
    }

    @Test
    public void testToDto() {
        AnalyticsEvent entity = new AnalyticsEvent();
        LocalDateTime createdAt = LocalDateTime.parse("2023-08-24T12:34:56");
        entity.setReceivedAt(createdAt);
        entity.setActorId(1L);
        entity.setReceiverId(2L);

        PostViewEventDto dto = mapper.toDto(entity);

        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(1L, dto.getUserId());
        assertEquals(2L, dto.getAuthorId());
    }
}
