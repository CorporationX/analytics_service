package faang.school.analytics.integration.repository;

import faang.school.analytics.integration.IntegrationTestBase;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class AnalyticsEventRepositoryIT extends IntegrationTestBase {
    private final AnalyticsEventRepository analyticsEventRepository;

    @Autowired
    AnalyticsEventRepositoryIT(AnalyticsEventRepository analyticsEventRepository) {
        this.analyticsEventRepository = analyticsEventRepository;
    }

    @Test
    void findByReceiverIdAndEventType() {
        var result = analyticsEventRepository.findByReceiverIdAndEventType(1, EventType.PROFILE_VIEW);
        assertThat(result).isNotNull().hasSize(2);
    }
}