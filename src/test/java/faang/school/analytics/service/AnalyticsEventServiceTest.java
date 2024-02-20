package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    private final long analyticsEventId = 1L;
    private final AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
            .id(analyticsEventId)
            .build();

    @Test
    void testSave() {
        analyticsEventService.save(analyticsEvent);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    void testDelete() {
        analyticsEventService.deleteById(analyticsEventId);

        verify(analyticsEventRepository, times(1)).deleteById(analyticsEventId);
    }

    @Test
    void testGetById_eventNotExist_throwsEntityNotFoundException() {
        long wrongId = analyticsEventId + 1;
        when(analyticsEventRepository.findById(wrongId)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> analyticsEventService.getById(wrongId)
        );
    }

    @Test
    void testGetById_eventExist_returnEvent() {
        when(analyticsEventRepository.findById(analyticsEventId)).thenReturn(Optional.of(analyticsEvent));

        AnalyticsEvent analyticsEventById = analyticsEventService.getById(analyticsEventId);

        assertEquals(analyticsEvent, analyticsEventById);
    }
}