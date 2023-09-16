package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.AnalyticsEventMapper;
import faang.school.analytics.dto.SearchAppearanceEventDto;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SearchAppearanceListener extends AbstractListener<SearchAppearanceEventDto> {
    public SearchAppearanceListener(ObjectMapper objectMapper,
                                    AnalyticsEventMapper analyticsEventMapper,
                                    AnalyticsEventRepository repository) {
        super(objectMapper, analyticsEventMapper, repository);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        SearchAppearanceEventDto searchAppearanceEventDto = readValue(message.getBody(), SearchAppearanceEventDto.class);
        log.info("Received new Search Appearance: {}", searchAppearanceEventDto);
        save(analyticsEventMapper.toSearchAppearanceEntity(searchAppearanceEventDto));
    }
}