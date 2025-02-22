package faang.school.analytics.service;

import faang.school.analytics.dto.event.CommentEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public void saveCreateComment(CommentEventDto commentEventDto) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(commentEventDto);
        analyticsEventRepository.save(analyticsEvent);
    @Transactional
    public void addEvent(@NonNull AnalyticsEvent event) {

        AnalyticsEvent analyticsEventSaved = analyticsEventRepository.save(event);
        log.info("Added event: {} ", analyticsEventSaved);
    }
}
