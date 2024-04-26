package faang.school.analytics.mappers;

import faang.school.analytics.dto.ProfileViewEventDto;
import org.springframework.stereotype.Component;


@Component
public class ProfileViewEventMapperAbstract extends AbstractAnalyticsEventMapper<ProfileViewEventDto> {
    public ProfileViewEventMapperAbstract(AnalyticsEventMapper<ProfileViewEventDto> mapper) {
        super(mapper);
    }

    @Override
    public boolean isSupported(Class<?> type) {
        return type == ProfileViewEventMapperAbstract.class;
    }
}
