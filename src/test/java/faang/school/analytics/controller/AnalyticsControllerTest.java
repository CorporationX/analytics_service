package faang.school.analytics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;

import static faang.school.analytics.model.EventType.POST_COMMENT;
import static org.hamcrest.Matchers.is;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Nested
@SpringBootTest
@AutoConfigureMockMvc
class AnalyticsEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnalyticsEventRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AnalyticsEventService analyticsEventService;


    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void getAllAnalyticsShouldReturnEmptyListWhenNoEvents() throws Exception {
        mockMvc.perform(get("/api/analytics")
                        .header("x-user-id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(0)));
    }


    @Test
    void getAllAnalyticsShouldReturnListOfAnalyticsDto() throws Exception {
        repository.deleteAll();
        AnalyticsEvent event1 = new AnalyticsEvent();
        event1.setActorId(10L);
        event1.setReceiverId(100L);
        event1.setReceivedAt(LocalDateTime.now().minusDays(1));
        event1.setEventType(POST_COMMENT);
        repository.save(event1);

        AnalyticsEvent event2 = new AnalyticsEvent();
        event2.setActorId(20L);
        event2.setReceiverId(200L);
        event2.setReceivedAt(LocalDateTime.now());
        event2.setEventType(POST_COMMENT);
        repository.save(event2);

        mockMvc.perform(get("/api/analytics")
                        .header("x-user-id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))

                .andExpect(jsonPath("$[0].postId", is((int) event1.getId())))
                .andExpect(jsonPath("$[0].actorId", is((int) event1.getActorId())))
                .andExpect(jsonPath("$[0].receiverId", is((int) event1.getReceiverId())))
                .andExpect(jsonPath("$[0].eventType", is(event1.getEventType().name())))

                .andExpect(jsonPath("$[1].postId", is((int) event2.getId())))
                .andExpect(jsonPath("$[1].actorId", is((int) event2.getActorId())))
                .andExpect(jsonPath("$[1].receiverId", is((int) event2.getReceiverId())))
                .andExpect(jsonPath("$[1].eventType", is(event2.getEventType().name())));

    }

    @Test
    void handleCommentEventShouldSaveEventAndReturnOk() throws Exception {
        repository.deleteAll();

        AnalyticsDto dto = new AnalyticsDto();
        dto.setPostId(1L);
        dto.setActorId(10L);
        dto.setReceiverId(100L);
        dto.setEventType(POST_COMMENT);
        dto.setReceivedAt(LocalDateTime.now());

        String requestBody = objectMapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(post("/api/analytics/comment-event")
                        .header("x-user-id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn();

        int status = result.getResponse().getStatus();
        String responseBody = result.getResponse().getContentAsString();

        System.out.println("Status = " + status);
        System.out.println("Response body = " + responseBody);

        assertEquals(200, status, "Response body: " + responseBody);

        List<AnalyticsEvent> events = (List<AnalyticsEvent>) repository.findAll();
        assertEquals(1, events.size());

        AnalyticsEvent saved = events.get(0);

        assertEquals(dto.getActorId(), saved.getActorId());
        assertEquals(dto.getReceiverId(), saved.getReceiverId());
        assertEquals(dto.getEventType(), saved.getEventType());

        assertNotNull(saved.getReceivedAt());
    }

    @Test
    void handleCommentEventShouldReturnBadRequestWhenBodyInvalid() throws Exception {
        String invalidBody = "{}";

        mockMvc.perform(post("/api/analytics/comment-event")
                        .header("x-user-id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());
    }
}