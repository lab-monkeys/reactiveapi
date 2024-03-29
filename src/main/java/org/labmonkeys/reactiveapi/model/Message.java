package org.labmonkeys.reactiveapi.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    UUID messageId;
    String message;
}
