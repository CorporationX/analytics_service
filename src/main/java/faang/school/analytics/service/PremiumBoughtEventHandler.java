package faang.school.analytics.service;

import faang.school.analytics.model.dto.PremiumBoughtEventDto;

public interface PremiumBoughtEventHandler {
    void handlePremiumBoughtEvent(PremiumBoughtEventDto event);
}