package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.event.PostViewEventDto;
import faang.school.analytics.mapper.PostViewEventMapper;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

class AnalyticPostViewEventServiceTest {

    @Mock
    private PostViewEventMapper postViewEventMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private AnalyticPostViewEventService analyticPostViewEventService;

    private PostViewEventDto postViewEventDto;
    private AnalyticsEventDto analyticsEventDto;

    @BeforeEach
    void setUp() {
        // Инициализация моков
        MockitoAnnotations.openMocks(this);

        postViewEventDto = new PostViewEventDto();
        postViewEventDto.setAuthorId(1L);
        postViewEventDto.setUserId(2L);

        analyticsEventDto = new AnalyticsEventDto();
        analyticsEventDto.setReceiverId(1L);
        analyticsEventDto.setActorId(2L);
        analyticsEventDto.setEventType(EventType.POST_VIEW);
        analyticsEventDto.setReceivedAt(LocalDateTime.now());

        // Настраиваем поведение маппера
        when(postViewEventMapper.toEntity(postViewEventDto)).thenReturn(analyticsEventDto);
    }

    @Test
    void savePostViewEvent_callsMapperAndSaveEvent() {
        // Вызов тестируемого метода
        analyticPostViewEventService.savePostViewEvent(postViewEventDto);

        // Проверка, что маппер был вызван
        verify(postViewEventMapper, times(1)).toEntity(postViewEventDto);

        // Проверка, что событие было сохранено через AnalyticsEventService
        verify(analyticsEventService, times(1)).saveEvent(analyticsEventDto);
    }

    @Test
    void savePostViewEvent_shouldSetReceivedAt() {
        // Вызов метода
        analyticPostViewEventService.savePostViewEvent(postViewEventDto);

        assertNotNull(analyticsEventDto.getReceivedAt());
        verify(analyticsEventService).saveEvent(analyticsEventDto);
    }
}