package org.labmonkeys.reactiveapi.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class MessageDto {
    UUID messageId;
    String message;
}
