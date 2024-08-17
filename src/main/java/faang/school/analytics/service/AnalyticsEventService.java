package faang.school.analytics.service;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.LikeEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validator.AnalyticsEventServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventServiceValidator analyticsEventServiceValidator;

    @Autowired
    public AnalyticsEventService(AnalyticsEventRepository analyticsEventRepository, AnalyticsEventMapper analyticsEventMapper, AnalyticsEventServiceValidator analyticsEventServiceValidator) {
        this.analyticsEventRepository = analyticsEventRepository;
        this.analyticsEventMapper = analyticsEventMapper;
        this.analyticsEventServiceValidator = analyticsEventServiceValidator;
    }

    public void saveLikeEvent(Message message) {
        analyticsEventServiceValidator.validateMessage(message);

        String[] data = message.toString().split(",");

        LikeEvent likeEvent = new LikeEvent();
        likeEvent.setPostId(Long.parseLong(data[0]));
        likeEvent.setAuthorId(Long.parseLong(data[1]));
        likeEvent.setUserId(Long.parseLong(data[2]));
        likeEvent.setTimestamp(LocalDateTime.parse(data[3]));

        AnalyticsEvent analyticsEvent = analyticsEventMapper.likeEventToAnalyticsEvent(likeEvent);
        analyticsEventRepository.save(analyticsEvent);
    }
}
