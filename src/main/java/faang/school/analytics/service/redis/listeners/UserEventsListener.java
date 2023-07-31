package faang.school.analytics.service.redis.listeners;

import faang.school.analytics.dto.AnalyticDto;
import faang.school.analytics.mapper.AnalyticsMapper;
import faang.school.analytics.service.analytics.AnalyticsService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserEventsListener implements MessageListener {
    @Getter
    @Value("${spring.data.redis.channels.user_events_channel.name}")
    private String channelName;

    private final AnalyticsService analyticsService;

    private final AnalyticsMapper analyticsMapper;
    private List<String> subscribedChannels = new ArrayList<>();

    @PostConstruct
    private void postConstruct() {
        subscribedChannels.add(channelName);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        AnalyticDto analyticDto = analyticsMapper.readValue(message.getBody(), AnalyticDto.class);

        analyticsService.create(analyticDto);

        log.info("Received message: " + "User with id: " + analyticDto.getId() + " was " + analyticDto.getType());
    }
}
