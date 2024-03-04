package faang.school.analytics.mapper;

import faang.school.analytics.dto.PremiumBoughtEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PremiumBoughtMapper extends AnalyticsEventMapper<PremiumBoughtEvent> {

    AnalyticsEvent toAnalyticsEvent(PremiumBoughtEvent event);

    @Override
    default Class<PremiumBoughtEvent> getType(){
        return PremiumBoughtEvent.class;
    }

}
