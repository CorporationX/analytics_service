package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceImplTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private AnalyticsServiceImpl analyticsServiceImpl;

    private AnalyticsEventDto analyticsEventDto;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() {
        analyticsEvent = AnalyticsEvent.builder()
                .id(4L)
                .actorId(1L)
                .receiverId(1L)
                .receivedAt(LocalDateTime.now())
                .build();

        analyticsEventDto = AnalyticsEventDto.builder()
                .id(4L)
                .actorId(1L)
                .receiverId(1L)
                .receivedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void save() {
        when(analyticsEventMapper.toEntity(analyticsEventDto)).thenReturn(analyticsEvent);
        when(analyticsEventRepository.save(analyticsEvent)).thenReturn(analyticsEvent);
        when(analyticsEventMapper.toDto(analyticsEvent)).thenReturn(analyticsEventDto);

        AnalyticsEventDto actual = analyticsServiceImpl.save(analyticsEventDto);
        assertEquals(analyticsEventDto, actual);

        InOrder inOrder = inOrder(analyticsEventRepository, analyticsEventMapper);
        inOrder.verify(analyticsEventMapper).toEntity(analyticsEventDto);
        inOrder.verify(analyticsEventRepository).save(analyticsEvent);
        inOrder.verify(analyticsEventMapper).toDto(analyticsEvent);
    }
}