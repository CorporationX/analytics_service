package faang.school.analytics.analyticsEventService;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.filter.AnalyticsFilterI;
import faang.school.analytics.mappers.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static faang.school.analytics.model.EventType.PROFILE_VIEW;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    public void saveEventPositiveTest() {
        AnalyticsEvent event = analyticsEventInitialize();
        AnalyticsEventDto analyticsEventDto = new AnalyticsEventDto();

        Mockito.when(analyticsEventMapper.toEntity(analyticsEventDto)).thenReturn(event);

        analyticsEventService.saveEvent(analyticsEventDto);

        Mockito.verify(analyticsEventRepository, Mockito.times(1)).save(any(AnalyticsEvent.class));
    }

    @Test
    public void saveEventNegativeTest() {

        AnalyticsEventDto analyticsEventDto = new AnalyticsEventDto();
        analyticsEventDto.setId(null);

        Mockito.when(analyticsEventMapper.toEntity(analyticsEventDto)).thenThrow(new MockitoException("Mapping failed"));

        try {
            analyticsEventService.saveEvent(analyticsEventDto);
        } catch (MockitoException e) {
            Mockito.verify(analyticsEventRepository, Mockito.times(0)).save(any(AnalyticsEvent.class));
        }
    }

    @Test
    public void getAnalyticsPositiveTest() {
        AnalyticsEvent event = analyticsEventInitialize();
        AnalyticsEventDto analyticsEventDto = new AnalyticsEventDto();
        analyticsEventDto.setId(1L);
        analyticsEventDto.setEventType(PROFILE_VIEW);
        analyticsEventDto.setActorId(123L);
        analyticsEventDto.setReceiverId(456L);
        analyticsEventDto.setReceivedAt(LocalDateTime.now());

        AnalyticsFilterI analyticsFilterIMock = Mockito.mock(AnalyticsFilterI.class);
        List<AnalyticsFilterI> analyticsFilters = List.of(analyticsFilterIMock);
        analyticsEventService = new AnalyticsEventService(analyticsEventRepository, analyticsEventMapper, analyticsFilters);

        Mockito.when(analyticsFilterIMock.isApplicable(any(AnalyticsFilterDto.class))).thenReturn(true);
        Mockito.when(analyticsFilterIMock.apply(any(Stream.class), any(AnalyticsFilterDto.class))).thenReturn(Stream.of(event));
        Mockito.when(analyticsEventMapper.toDto(any(AnalyticsEvent.class))).thenReturn(analyticsEventDto);

        AnalyticsFilterDto analyticsFilterDto = new AnalyticsFilterDto();
        analyticsFilterDto.setEventType(PROFILE_VIEW);
        analyticsFilterDto.setReceiverId(456L);
        analyticsFilterDto.setFrom(LocalDateTime.now());
        analyticsFilterDto.setTo(LocalDateTime.now());

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(analyticsFilterDto);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(analyticsEventDto.getId(), result.get(0).getId());
    }

    @Test
    public void getAnalyticsNegativeEmptyTest() {
        AnalyticsEvent event = analyticsEventInitialize();
        AnalyticsEventDto analyticsEventDto = new AnalyticsEventDto();
        analyticsEventDto.setId(1L);
        analyticsEventDto.setEventType(PROFILE_VIEW);
        analyticsEventDto.setActorId(123L);
        analyticsEventDto.setReceiverId(456L);
        analyticsEventDto.setReceivedAt(LocalDateTime.now());

        AnalyticsFilterI analyticsFilterIMock = Mockito.mock(AnalyticsFilterI.class);
        List<AnalyticsFilterI> analyticsFilters = List.of(analyticsFilterIMock);
        analyticsEventService = new AnalyticsEventService(analyticsEventRepository, analyticsEventMapper, analyticsFilters);

        Mockito.when(analyticsFilterIMock.isApplicable(any(AnalyticsFilterDto.class))).thenReturn(false);
        AnalyticsFilterDto analyticsFilterDto = new AnalyticsFilterDto();
        analyticsFilterDto.setEventType(PROFILE_VIEW);
        analyticsFilterDto.setReceiverId(456L);
        analyticsFilterDto.setFrom(LocalDateTime.now());
        analyticsFilterDto.setTo(LocalDateTime.now());

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(analyticsFilterDto);

        System.out.println("Result: " + result);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    private AnalyticsEvent analyticsEventInitialize() {
        return AnalyticsEvent.builder()
                .id(1L)
                .eventType(PROFILE_VIEW)
                .actorId(123L)
                .receiverId(456L)
                .receivedAt(LocalDateTime.now())
                .build();
    }
}
