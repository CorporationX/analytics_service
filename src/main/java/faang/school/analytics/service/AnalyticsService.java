package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;

public interface AnalyticsService {

    void save(AnalyticsEvent event);
}
//import faang.school.analytics.event.profile.ProfileViewEvent;
//import faang.school.analytics.mapper.profile.ProfileViewEventMapper;
//import faang.school.analytics.model.AnalyticsEvent;
//import faang.school.analytics.model.EventType;
//import faang.school.analytics.repository.AnalyticsEventRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class AnalyticsService {
//    private final ProfileViewEventMapper profileViewEventMapper;
//    private final AnalyticsEventRepository analyticsEventRepository;
//
//    public void saveEvent(EventType eventType, AnalyticsEvent analyticsEvent) {
//        analyticsEvent.setEventType(eventType);
//        analyticsEventRepository.save(analyticsEvent);
//        log.info(analyticsEvent + " is saved to DB");
//    }
//
//    public void saveProfileViewEvent(ProfileViewEvent profileViewEvent) {
//        AnalyticsEvent analyticsEvent = profileViewEventMapper.toAnalyticsEvent(profileViewEvent);
//        saveEvent(EventType.PROFILE_VIEW, analyticsEvent);
//    }
//}