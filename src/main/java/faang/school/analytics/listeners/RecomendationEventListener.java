package faang.school.analytics.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.RecomendationEventDto;
import faang.school.analytics.service.AnalyticService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecomendationEventListener extends AbstractEventListener<RecomendationEventDto> {


    public RecomendationEventListener(ObjectMapper objectMapper, List<AnalyticService<RecomendationEventDto>> analyticServiceList) {
        super(objectMapper, analyticServiceList);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RecomendationEventDto recomendationEventDto = convertToJSON(message, RecomendationEventDto.class);
        save(recomendationEventDto);
    }
}
