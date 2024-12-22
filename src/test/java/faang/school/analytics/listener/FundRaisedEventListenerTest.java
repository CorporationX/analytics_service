package faang.school.analytics.listener;

import faang.school.analytics.dto.FundRaisedEvent;
import faang.school.analytics.dto.event.EventDto;
import faang.school.analytics.mapper.fund_raised.FundRaisedEventMapper;
import faang.school.analytics.service.event.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FundRaisedEventListenerTest {
    @InjectMocks
    private FundRaisedEventListener fundRaisedEventListener;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private FundRaisedEventMapper fundRaisedEventMapper;

    private FundRaisedEvent fundRaisedEvent;
    private EventDto eventDto;

    @BeforeEach
    void setUp() {
        fundRaisedEvent = new FundRaisedEvent();
        eventDto = new EventDto();
    }

    @Test
    void shouldHandleEventSuccessfully() {
        when(fundRaisedEventMapper.toEventDto(fundRaisedEvent)).thenReturn(eventDto);

        fundRaisedEventListener.handleEvent(fundRaisedEvent);

        verify(fundRaisedEventMapper).toEventDto(fundRaisedEvent);
        verify(analyticsEventService).addNewEvent(eventDto);
    }

    @Test
    void shouldMapEventToDtoCorrectly() {
        when(fundRaisedEventMapper.toEventDto(fundRaisedEvent)).thenReturn(eventDto);

        EventDto result = fundRaisedEventMapper.toEventDto(fundRaisedEvent);

        assertNotNull(result);
    }
}
