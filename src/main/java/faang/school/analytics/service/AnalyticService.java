package faang.school.analytics.service;

import faang.school.analytics.event.ProjectViewEvent;

public interface AnalyticService {

    void saveAnalyticEvent(ProjectViewEvent event);
}
