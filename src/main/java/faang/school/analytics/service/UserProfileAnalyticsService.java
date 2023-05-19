package faang.school.analytics.service;

import faang.school.analytics.dto.Interval;
import faang.school.analytics.dto.UserProfileViewDto;
import faang.school.analytics.mapper.UserProfileViewMapper;
import faang.school.analytics.dto.event.UserProfileViewEvent;
import faang.school.analytics.model.UserProfileView;
import faang.school.analytics.repository.UserProfileViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserProfileAnalyticsService {

    private final UserProfileViewRepository userProfileViewRepository;
    private final UserProfileViewMapper userProfileViewMapper;

    @Transactional
    public void saveView(UserProfileViewEvent event) {
        UserProfileView view = userProfileViewMapper.toEntity(event);
        userProfileViewRepository.save(view);
    }

    @Transactional(readOnly = true)
    public List<UserProfileViewDto> getProfileViewAnalytics(long userId, Interval interval,
                                                            LocalDateTime from, LocalDateTime to) {
        Stream<UserProfileView> views = userProfileViewRepository.findByUserId(userId);
        if (interval != null) {
            views = views.filter(view -> view.getViewedAt().isAfter(interval.getFrom()));
        } else {
            views = views.filter(view -> view.getViewedAt().isAfter(from) && view.getViewedAt().isBefore(to));
        }
        return views.sorted((v1, v2) -> v2.getViewedAt().compareTo(v1.getViewedAt()))
                .map(userProfileViewMapper::toDto)
                .toList();
    }
}
