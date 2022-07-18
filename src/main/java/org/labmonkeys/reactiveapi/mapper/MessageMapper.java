package org.labmonkeys.reactiveapi.mapper;

import java.util.List;

import org.labmonkeys.reactiveapi.dto.MessageDto;
import org.labmonkeys.reactiveapi.model.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface MessageMapper {

    Message dtoToMessage(MessageDto dto);
    MessageDto messageToDto(Message message);
    List<Message> dtosToMessages(List<MessageDto> dtos);
    List<MessageDto> messagesToDtos(List<Message> messages);
    
}
