package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.MentorshipRequestedEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MentorshipRequestedEventMapperTest {
    private final MentorshipRequestedEventMapper mapper = Mappers.getMapper(MentorshipRequestedEventMapper.class);

    @Test
    void testToEntityMapping() {
        MentorshipRequestedEventDto dto = MentorshipRequestedEventDto.builder()
                .requesterId(1L)
                .receiverId(2L)
                .createdAt(LocalDateTime.parse("2020-01-01T00:00:00"))
                .build();

        AnalyticsEvent entity = mapper.toEntity(dto);

        assertEquals(dto.getRequesterId(), entity.getActorId());
        assertEquals(dto.getReceiverId(), entity.getReceiverId());
        assertEquals(dto.getCreatedAt(), entity.getReceivedAt());
    }

    @Test
    void testToDtoMapping() {
        MentorshipRequestedEventDto dto = MentorshipRequestedEventDto.builder()
                .requesterId(123L)
                .receiverId(456L)
                .createdAt(LocalDateTime.parse("2023-08-14T12:00:00"))
                .build();

        AnalyticsEventDto analyticsDto = mapper.toDto(dto);

        assertEquals(dto.getRequesterId(), analyticsDto.getActorId());
        assertEquals(dto.getReceiverId(), analyticsDto.getReceiverId());
        assertEquals(dto.getCreatedAt(), analyticsDto.getReceivedAt());
    }
}