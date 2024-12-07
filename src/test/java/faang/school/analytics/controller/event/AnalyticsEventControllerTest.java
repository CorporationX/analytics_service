package faang.school.analytics.controller.event;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.EventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.dto.event.Interval;
import faang.school.analytics.service.event.AnalyticsEventService;
import faang.school.analytics.service.event.EventParamService;
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

    @Mock
    private EventParamService eventParamService;

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

        mockMvc.perform(post("/api/v1/events")
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

        when(eventParamService.getEventsDto(1, "FOLLOWER", "DAY", null, null)).thenReturn(array);

        mockMvc.perform(get("/api/v1/events/get")
                        .param("receiverId", "1")
                        .param("eventType", "FOLLOWER")
                        .param("interval", "DAY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventType", is("FOLLOWER")))
                .andExpect(jsonPath("$[1].eventType", is("FOLLOWER")));
    }
}