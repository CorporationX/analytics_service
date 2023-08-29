package faang.school.analytics;

import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.util.JsonMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = AnalyticsServiceApp.class)
@AutoConfigureMockMvc
public class AnalyticsControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JsonMapper jsonMapper;
    @Test
    void getAnalyticsTest() throws Exception {
        AnalyticsFilterDto filterDto = new AnalyticsFilterDto(1L, EventType.FOLLOWER, null, null);
        mvc.perform(post("/analytics/get/filtered")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.toJson(filterDto).get()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAnalyticsWrongFilterTest() throws Exception {
        AnalyticsFilterDto filterDto = new AnalyticsFilterDto();

        mvc.perform(post("/analytics/get/filtered")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.toJson(filterDto).get()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
