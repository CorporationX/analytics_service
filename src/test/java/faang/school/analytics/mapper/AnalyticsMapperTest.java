package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDTO;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnalyticsMapperTest {

    private final AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);
    private AnalyticsEventDTO dto;
    private AnalyticsEvent entity;

    @BeforeEach
    void setUp() {
        dto = AnalyticsEventDTO.builder()
                .id(1)
                .eventType(EventType.PROFILE_VIEW)
                .receiverId(5)
                .build();


        entity = AnalyticsEvent.builder()
                .id(1)
                .eventType(EventType.PROFILE_VIEW)
                .receiverId(5)
                .build();
    }

    @Test
    @DisplayName("The test must returned dto")
    void testMappingToDtoSuccess() {
        AnalyticsEventDTO result = analyticsEventMapper.toDto(entity);

        Assertions.assertEquals(dto, result);
    }

    @Test
    @DisplayName("The test must returned entity")
    void testMappingToEntitySuccess() {
        AnalyticsEvent result = analyticsEventMapper.toEntity(dto);

        Assertions.assertEquals(entity, result);
    }

    @Test
    @DisplayName("The test must returned null when entity is null")
    void testMappingToDtoWithNullEntity() {
        Assertions.assertNull(analyticsEventMapper.toDto(null));
    }

    @Test
    @DisplayName("The test must returned null when dto is null")
    void testMappingToEntityWithNullDto() {
        Assertions.assertNull(analyticsEventMapper.toEntity(null));
    }
}
