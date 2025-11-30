package faang.school.analytics.service;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Service
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @SuppressWarnings("checkstyle:CommentsIndentation")
    public List<AnalyticsEvent> getAnalytics(long receiverId, EventType eventType,
                                             Interval interval, LocalDateTime from, LocalDateTime to) {
        analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
//       Далее из полученного набора объектов нужно оставить лишь те,
//       что попадают либо в переданный interval, либо,
//       если interval = null, то попадают в период от from до to. - условие задачи

        /* не понимаю, как реализовать фильтрацию, описанную выше, подскажи пожалуйста */
        return null;
    }

    public void saveEvent(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save(analyticsEvent);
    }
}
