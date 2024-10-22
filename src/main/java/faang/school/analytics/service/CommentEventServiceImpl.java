package faang.school.analytics.service;

import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentEventServiceImpl implements CommentEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper mapper;

    @Override
    public void save(CommentEvent event) {
        analyticsEventRepository.save(mapper.toAnalytics(event));
    }
}
