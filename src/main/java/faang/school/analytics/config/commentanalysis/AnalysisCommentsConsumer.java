package faang.school.analytics.config.commentanalysis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.commentanalysis.AnalysisCommentsEventDto;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnalysisCommentsConsumer {

    private final AnalyticsEventService analyticsService;
    private final ObjectMapper objectMapper;

    public AnalysisCommentsConsumer(AnalyticsEventService analyticsService,
                                    @Qualifier("objectMapperAnalysisComments") ObjectMapper objectMapper) {
        this.analyticsService = analyticsService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${kafka.consumers.comment.topic}",
            containerFactory = "concurrentKafkaListenerContainerFactoryAnalysisComments",
            groupId = "${kafka.consumers.comment.group-id}")
    public void onMessage(String event, Acknowledgment acknowledgment) {
        try {
            log.info("Received Analysis Comments Event from Post Service: {}", event);
            AnalysisCommentsEventDto dto = objectMapper.readValue(event, AnalysisCommentsEventDto.class);
            analyticsService.handleEvent(dto);
            acknowledgment.acknowledge();

            log.info("Successfully processed and acknowledged Analysis Event [commentId={}]", dto.commentId());
        } catch (Exception e) {
            log.error("Error processing Analysis Comments Event: ", e);
        }
    }
}