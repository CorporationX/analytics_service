package faang.school.analytics.mapper.search_appearance_event;

import faang.school.analytics.dto.SearchAppearanceEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-29T00:12:55+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class AnalyticsEventMapperImpl implements AnalyticsEventMapper {

    @Override
    public AnalyticsEvent toEntity(SearchAppearanceEventDto searchAppearanceEventDto) {
        if ( searchAppearanceEventDto == null ) {
            return null;
        }

        AnalyticsEvent.AnalyticsEventBuilder analyticsEvent = AnalyticsEvent.builder();

        analyticsEvent.receiverId( searchAppearanceEventDto.getReceiverId() );
        analyticsEvent.actorId( searchAppearanceEventDto.getActorId() );
        analyticsEvent.eventType( searchAppearanceEventDto.getEventType() );
        analyticsEvent.receivedAt( searchAppearanceEventDto.getReceivedAt() );

        return analyticsEvent.build();
    }

    @Override
    public SearchAppearanceEventDto toDto(AnalyticsEvent analyticsEvent) {
        if ( analyticsEvent == null ) {
            return null;
        }

        SearchAppearanceEventDto.SearchAppearanceEventDtoBuilder searchAppearanceEventDto = SearchAppearanceEventDto.builder();

        searchAppearanceEventDto.receiverId( analyticsEvent.getReceiverId() );
        searchAppearanceEventDto.actorId( analyticsEvent.getActorId() );
        searchAppearanceEventDto.eventType( analyticsEvent.getEventType() );
        searchAppearanceEventDto.receivedAt( analyticsEvent.getReceivedAt() );

        return searchAppearanceEventDto.build();
    }
}
