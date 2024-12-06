package faang.school.analytics.controller.event;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.EventDto;
import faang.school.analytics.dto.event.EventRequestDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.dto.event.Interval;
import faang.school.analytics.service.event.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@ExtendWith(MockitoExtension.class)
class AnalyticsEventControllerTest {
    @InjectMocks
    private AnalyticsEventController analyticsEventController;
    @Mock
    private AnalyticsEventService analyticsEventService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(analyticsEventController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testAddNewEvent() throws Exception {
        EventDto eventDto = new EventDto();

        eventDto.setEventType(EventType.FOLLOWER);
        eventDto.setActorId(1);
        eventDto.setReceiverId(2);


        when(analyticsEventService.addNewEvent(eventDto)).thenReturn(eventDto);

        mockMvc.perform(post("/api/v1/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actorId", is(1)))
                .andExpect(jsonPath("$.receiverId", is(2)))
                .andExpect(jsonPath("$.eventType", is("FOLLOWER")));
    }


    @Test
    public void testGetEvents() throws Exception {
        EventDto firstEvent = new EventDto();
        EventDto secondEvent = new EventDto();

        List<EventDto> array = new ArrayList<>();
        Stream.of(firstEvent, secondEvent).forEach(event -> {
            event.setEventType(EventType.FOLLOWER);
            event.setReceiverId(1);
            event.setActorId(2);
            array.add(event);
        });

        EventRequestDto eventRequestDto = new EventRequestDto();
        eventRequestDto.setEventType(EventType.FOLLOWER);
        eventRequestDto.setInterval(Interval.DAY);

        when(analyticsEventService.getEventsDto(eventRequestDto)).thenReturn(array);

        mockMvc.perform(post("/api/v1/event/get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventType", is("FOLLOWER")))
                .andExpect(jsonPath("$[1].eventType", is("FOLLOWER")));
    }
}