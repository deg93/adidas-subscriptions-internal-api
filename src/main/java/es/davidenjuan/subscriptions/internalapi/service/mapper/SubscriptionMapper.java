package es.davidenjuan.subscriptions.internalapi.service.mapper;

import org.mapstruct.Mapper;

import es.davidenjuan.subscriptions.internalapi.domain.Subscription;
import es.davidenjuan.subscriptions.internalapi.service.dto.SubscriptionDTO;

@Mapper(componentModel = "spring", uses = {})
public interface SubscriptionMapper {

    Subscription toEntity(SubscriptionDTO dto);

    SubscriptionDTO toDto(Subscription entity);
}
