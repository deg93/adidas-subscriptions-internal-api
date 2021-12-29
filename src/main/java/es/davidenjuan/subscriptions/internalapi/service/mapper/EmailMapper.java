package es.davidenjuan.subscriptions.internalapi.service.mapper;

import org.mapstruct.Mapper;

import es.davidenjuan.subscriptions.internalapi.domain.Email;
import es.davidenjuan.subscriptions.internalapi.service.dto.EmailDTO;

@Mapper(componentModel = "spring", uses = {})
public interface EmailMapper {

    Email toEntity(EmailDTO dto);

    EmailDTO toDto(Email entity);
}
